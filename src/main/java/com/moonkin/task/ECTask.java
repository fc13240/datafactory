package com.moonkin.task;

import com.moonkin.decode.ECParser;
import com.moonkin.ui.MainWindow;
import com.moonkin.util.FileUtil;
import com.moonkin.util.JarToolUtil;
import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * EC数据文件合并转NC，调用Python
 * xuduo
 * 2018/9/26
 */
public class ECTask extends BaseTask {

    private static Logger logger = LoggerFactory.getLogger(ECTask.class);

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private static Pattern fileNamePattern = Pattern.compile("echirs_(height|rh|wind|temper)_(500|700|850|925)_.*");

    @Override
    protected void config() {
        setTaskId("ec_task");
        setTaskName("欧细数据文件合并");
    }

    @Override
    protected void doWork(JobDataMap dataMap) {
        logger.info("EC任务开始执行");
        String statusFileName = JarToolUtil.getJarDir() + File.separator + "ec.txt";
        byte[] b = new byte[2];
        b[0] = 0x0d;
        b[1] = 0x0a;
        String c_string = new String(b);
        String sourceFolder = MainWindow.mainWindow.getEcSourceTextField().getText().trim();
        String destFolder = MainWindow.mainWindow.getEcDestTextField().getText().trim();
        File srcDir = new File(sourceFolder);
        if (!srcDir.exists()) {
            logger.info("EC数据目录不存在，或连接超时");
            return;
        }
        if (srcDir.exists() && srcDir.isDirectory()) {
            File statusFile = new File(statusFileName);
            // 状态文件2小时重写
            if (statusFile.exists()) {
                statusFile.delete();
            }
            //文件不存在创建状态记录文件
            if (!statusFile.exists()) {
                System.out.println("正在初始化状态文件 " + statusFileName + "... ...");
                System.out.println("start:" + LocalDateTime.now());
                String[] fileNames = srcDir.list();
                fileNames = Arrays.stream(fileNames).sorted((f1,f2) -> {
                    if(f1.compareTo(f2) > 0) {
                        return -1;
                    } else {
                        return 1;
                    }
                }).toArray(String[]::new);
                // 由于文件量过大，先获取文件名称列表，写入状态文件，再处理。
                StringBuilder logsb = new StringBuilder();
                int n = 0;
                for (int i = 0; i < fileNames.length; i ++) {  //文件名 状态 时间戳 初始化时间最早
                    if(fileNamePattern.matcher(fileNames[i]).matches()) {
                        logsb.append(fileNames[i]).append(" ").append("0").append(" ").append(LocalDateTime.now().format(dtf)).append(c_string);
                        n++;
                        if(n > 500) {
                            break;
                        }
                    }
                }
                FileUtil.write(statusFileName, logsb.toString(), "UTF-8");
                System.out.println("end:" + LocalDateTime.now());
            }
            BufferedReader br;
            try {
                br = new BufferedReader(new FileReader(JarToolUtil.getJarDir() + File.separator + "ec.txt"));
                String line;
                StringBuilder sb = new StringBuilder();
                while (null != (line = br.readLine())) {
                    String[] status = line.split("\\s+");
                    if (status.length == 3) { //文件名 状态 时间
                        long last = new File(sourceFolder + File.separator + status[0]).lastModified();
                        //当状态是0 或者文件的修改时间晚于记录时间时，则处理文件
                        if (status[1].equals("0") &&
                                LocalDateTime.ofInstant(Instant.ofEpochMilli(last), ZoneId.systemDefault()).isAfter(LocalDateTime.parse(status[2], dtf))) { //只处理状态是 0 的文件
                            logger.info("EC开始处理，当前文件是" + status[0]);
                            sb.append("[EC]");
                            sb.append("[").append(LocalDateTime.now().toString()).append("]:").append("正在处理文件 ")
                                    .append(status[0]).append(",转换后文件为").append(destFolder).append(c_string);
                            MainWindow.mainWindow.getOutputTextArea().setText(sb.toString());
                            //生成文件位置，按08和20分目录
                            String name = status[0];
                            Pattern p = Pattern.compile("echirs_.*");
                            Matcher m = p.matcher(name);
                            if (!m.find()) {
                                continue;
                            }
                            String subfold = name.substring(name.lastIndexOf(".") - 4, name.lastIndexOf(".") - 2);
                            String dest = destFolder + File.separator + subfold;
                            if (!new File(dest).exists()) {
                                new File(dest).mkdirs();
                            }
                            try {
                                ECParser.file_trans_to_nc(sourceFolder + File.separator, name, dest + File.separator);
                                //脚本参数
                                //PythonUtil.runPythonFile(JarToolUtil.getJarDir() + "/trans2nc/read_ec.py", new String[]{sourceFolder + File.separator, name, dest + File.separator});
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (!MainWindow.mainWindow.getOutputTextArea().getText().isEmpty()) {
                                sb.append(MainWindow.mainWindow.getOutputTextArea().getText()).append(c_string);
                            }
                            if (sb.length() >= 1000) { //清空
                                MainWindow.mainWindow.getOutputTextArea().setText("");
                                sb.delete(0, sb.length());
                            }
                            sb.append("[EC]");
                            sb.append("[").append(LocalDateTime.now().toString()).append("]:").append("文件")
                                    .append(status[0]).append("处理完毕").append(c_string);
                            MainWindow.mainWindow.getOutputTextArea().setText(sb.toString());
                            //修改对应的记录行
                            FileUtil.modifyFileContent(statusFileName, line, status[0] + " " + "1" + " " + LocalDateTime.ofInstant(Instant.ofEpochMilli(last), ZoneId.systemDefault()).format(dtf));
                            logger.info("状态文件修改完毕，当前状态是" + status[0] + " " + "1" + " " + LocalDateTime.ofInstant(Instant.ofEpochMilli(last), ZoneId.systemDefault()).format(dtf));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("EC处理异常，" + e.getMessage() + LocalDateTime.now());
            }
        }
    }
}
