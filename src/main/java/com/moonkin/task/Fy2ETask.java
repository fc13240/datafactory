package com.moonkin.task;

import com.moonkin.ui.MainWindow;
import com.moonkin.util.FileUtil;
import com.moonkin.util.JarToolUtil;
import org.quartz.JobDataMap;

import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * xuduo
 * 2018/9/26
 */
public class Fy2ETask extends BaseTask {

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    protected void config() {
        setTaskId("fy2e_task");
        setTaskName("风云2E数据分通道");
    }

    @Override
    protected void doWork(JobDataMap dataMap) {
        String statusFileName = JarToolUtil.getJarDir() + File.separator + "fy2e.txt";
        byte[] b = new byte[2];
        b[0] = 0x0d;
        b[1] = 0x0a;
        String c_string = new String(b);
        String sourceFolder = MainWindow.mainWindow.getFy2eSourceTextField().getText().trim();
        String destFolder = MainWindow.mainWindow.getFy2eDestTextField().getText().trim();
        //脚本位置
        File srcDir = new File(sourceFolder);
        if (!srcDir.exists()) {
            return;
        }
        if (srcDir.exists() && srcDir.isDirectory()) {
            File statusFile = new File(statusFileName);
            // 状态文件2小时重写
            if (statusFile.exists()
                    && LocalDateTime.ofInstant(Instant.ofEpochMilli(statusFile.lastModified()),
                    ZoneId.systemDefault()).isBefore(LocalDateTime.now().minusHours(2))) {
                String line;
                BufferedReader br;
                try {
                    br = new BufferedReader(new FileReader(statusFileName));
                    List<String> oldlist = new ArrayList<>();
                    //读取原状态文件内容，再把旧文件去掉
                    while (null != (line = br.readLine())) {
                        String[] status = line.split("\\s+");
                        if (status[1].equals("1")) {
                            oldlist.add(status[0]);
                        }
                    }
                    //读取就旧数据，然后删除新建
                    statusFile.delete();
                    String[] fileNames = srcDir.list();
                    List<String> newlist = Arrays.asList(fileNames);
                    newlist.removeAll(oldlist);
                    StringBuilder logsb = new StringBuilder();
                    for (String fileName : newlist) {  //文件名 状态 时间戳 初始化时间最早
                        if (fileName.endsWith("_lbt_fy2e.jpg")) {
                            logsb.append(fileName).append(" ").append("0").append(" ").append(LocalDateTime.now().minusHours(3).format(dtf)).append(c_string);
                        }
                    }
                    FileUtil.write(statusFileName, logsb.toString(), "UTF-8");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            //文件不存在创建状态记录文件
            if (!statusFile.exists()) {
                System.out.println("正在初始化状态文件 " + statusFileName + "... ...");
                String[] fileNames = srcDir.list();
                // 由于文件量过大，先获取文件名称列表，写入状态文件，再处理。
                StringBuilder logsb = new StringBuilder();
                for (String fileName : fileNames) {  //文件名 状态 时间戳 初始化时间最早
                    if (fileName.endsWith("_lbt_fy2e.jpg")) {
                        logsb.append(fileName).append(" ").append("0").append(" ").append(LocalDateTime.now().minusHours(3).format(dtf)).append(c_string);
                    }
                }
                FileUtil.write(statusFileName, logsb.toString(), "UTF-8");
            }
            BufferedReader br;
            try {
                br = new BufferedReader(new FileReader(JarToolUtil.getJarDir() + File.separator + "fy2e.txt"));
                String line;
                while (null != (line = br.readLine())) {
                    StringBuilder sb = new StringBuilder();
                    String[] status = line.split("\\s+");
                    if (status.length == 3) { //文件名 状态 时间
                        long last = new File(sourceFolder + File.separator + status[0]).lastModified();
                        //当状态是0 或者文件的修改时间晚于记录时间时，则处理文件
                        if (status[1].equals("0") ||
                                LocalDateTime.ofInstant(Instant.ofEpochMilli(last), ZoneId.systemDefault()).isAfter(LocalDateTime.parse(status[2], dtf))) { //只处理状态是 0 的文件
                            if (!MainWindow.mainWindow.getOutputTextArea().getText().isEmpty()) {
                                sb.append(MainWindow.mainWindow.getOutputTextArea().getText()).append(c_string);
                            }
                            if (sb.length() >= 1000) { //清空
                                MainWindow.mainWindow.getOutputTextArea().setText("");
                                sb.delete(0, sb.length());
                            }
                            sb.append("[风云2E]");
                            sb.append("[").append(LocalDateTime.now().toString()).append("]:").append("正在处理文件")
                                    .append(sourceFolder).append(",转换后文件为").append(destFolder).append(c_string);
                            MainWindow.mainWindow.getOutputTextArea().setText(sb.toString());
                            //201809131532_ch6_lbt_fy2e.kj
                            //换名
                            String temp = status[0].replaceAll("_lbt_fy2e.jpg", "");
                            temp = temp.substring(temp.indexOf("_") + 1);
                            File destFile = new File(destFolder + "/" + temp + "/" + status[0]);
                            try {
                                FileUtil.fileChannelCopy(new File(srcDir + File.separator + status[0]), destFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            sb.append("[").append(LocalDateTime.now().toString()).append("]:").append("文件")
                                    .append(status[0]).append("处理完毕");
                            MainWindow.mainWindow.getOutputTextArea().setText(sb.toString());
                            //修改对应的记录行
                            FileUtil.modifyFileContent(statusFileName, line, status[0] + " " + "1" + " " + LocalDateTime.ofInstant(Instant.ofEpochMilli(last), ZoneId.systemDefault()).format(dtf));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
