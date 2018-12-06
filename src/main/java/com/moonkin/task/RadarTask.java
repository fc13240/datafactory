package com.moonkin.task;

import com.moonkin.ui.MainWindow;
import com.moonkin.util.FileUtil;
import com.moonkin.util.JarToolUtil;
import com.moonkin.util.PythonUtil;
import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * 雷达图拷贝任务
 * xuduo
 * 2018/9/26
 */
public class RadarTask extends BaseTask {

    private static Logger logger = LoggerFactory.getLogger(RadarTask.class);

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private static Pattern fileNamePattern = Pattern.compile("Z_RADA_C_BABJ_.*_CR.*");

    @Override
    protected void config() {
        setTaskId("radar_task");
        setTaskName("雷达图分通道");
    }

    @Override
    protected void doWork(JobDataMap dataMap) {
        logger.info("雷达图转数据任务开始");
        String statusFileName = JarToolUtil.getJarDir() + File.separator + "radar.txt";
        byte[] b = new byte[2];
        b[0] = 0x0d;
        b[1] = 0x0a;
        String c_string = new String(b);
        String sourceFolder = MainWindow.mainWindow.getRadarSourceTextField().getText().trim();
        String destFolder = MainWindow.mainWindow.getRadarDestTextField().getText().trim();
        File srcDir = new File(sourceFolder);
        if (!srcDir.exists()) {
            logger.info("雷达图目录不存在，或连接超时");
            return;
        }
        if (srcDir.exists() && srcDir.isDirectory()) {
            File statusFile = new File(statusFileName);
            // 状态文件2小时重写
            if(statusFile.exists()) {
                statusFile.delete();
            }
            //文件不存在创建状态记录文件
            if (!statusFile.exists()) {
                System.out.println("正在初始化状态文件 " + statusFileName + "... ...");
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
                        logsb.append(fileNames[i]).append(" ").append("0").append(" ").append(LocalDateTime.now().minusHours(3).format(dtf)).append(c_string);
                        n++;
                        if(n > 500) {
                            break;
                        }
                    }
                }
                FileUtil.write(statusFileName, logsb.toString(), "UTF-8");
            }
            BufferedReader br;
            try {
                br = new BufferedReader(new FileReader(JarToolUtil.getJarDir() + File.separator + "radar.txt"));
                String line;
                StringBuilder sb = new StringBuilder();
                while (null != (line = br.readLine())) {
                    String[] status = line.split("\\s+");
                    if (status.length == 3) { //文件名 状态 时间
                        long last = new File(sourceFolder + File.separator + status[0]).lastModified();
                        //当状态是0 或者文件的修改时间晚于记录时间时，则处理文件
                        if (status[1].equals("0")) { //只处理状态是 0 的文件
                            logger.info("雷达图文件开始处理，当前文件是" + status[0]);
                            sb.append("[雷达]");
                            sb.append("[").append(LocalDateTime.now().toString()).append("]:").append("正在处理文件 ")
                                    .append(status[0]).append(",转换后文件为").append(destFolder).append(c_string);
                            MainWindow.mainWindow.getOutputTextArea().setText(sb.toString());
                            //生成文件位置，按08和20分目录
                            String name = status[0];
                            String dest = destFolder;
                            if (!new File(dest).exists()) {
                                new File(dest).mkdirs();
                            }
                            System.out.println("start: " + LocalDateTime.now());
                            try {
                                //脚本参数
                                PythonUtil.runPythonFile(JarToolUtil.getJarDir() + "\\trans2nc\\main_pic2data_linux.py", new String[]{sourceFolder + File.separator + name, JarToolUtil.getJarDir() + "\\trans2nc", dest});
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            System.out.println("end: " + LocalDateTime.now());

                            if (!MainWindow.mainWindow.getOutputTextArea().getText().isEmpty()) {
                                sb.append(MainWindow.mainWindow.getOutputTextArea().getText()).append(c_string);
                            }
                            if (sb.length() >= 1000) { //清空
                                MainWindow.mainWindow.getOutputTextArea().setText("");
                                sb.delete(0, sb.length());
                            }
                            sb.append("[雷达]");
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
                logger.error("雷达任务异常," + e.getMessage() + LocalDateTime.now());
            }
        }
    }
}