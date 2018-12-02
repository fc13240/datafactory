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
 * 日本传真数据换名任务
 * xuduo
 * 2018/9/26
 */
public class JapanTask extends BaseTask {

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    protected void config() {
        setTaskId("japan_task");
        setTaskName("日本传真数据改名");
    }

    @Override
    protected void doWork(JobDataMap dataMap) {
        String statusFileName = JarToolUtil.getJarDir() + File.separator + "japan.txt";
        byte[] b = new byte[2];
        b[0] = 0x0d;
        b[1] = 0x0a;
        String c_string = new String(b);
        String sourceFolder = MainWindow.mainWindow.getJapanSourceTextField().getText().trim();
        String destFolder = MainWindow.mainWindow.getJapanDestTextField().getText().trim();
        //脚本位置
        File srcDir = new File(sourceFolder);
        if (!srcDir.exists()) {
            return;
        }
        if (srcDir.exists() && srcDir.isDirectory()) {
            File statusFile = new File(statusFileName);
            // 状态文件2小时重写
            if(statusFile.exists()
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
                        if(status[1].equals("1")) {
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
                        logsb.append(fileName).append(" ").append("0").append(" ").append(LocalDateTime.now().minusHours(3).format(dtf)).append(c_string);
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
                    logsb.append(fileName).append(" ").append("0").append(" ").append(LocalDateTime.now().minusHours(3).format(dtf)).append(c_string);
                }
                FileUtil.write(statusFileName, logsb.toString(), "UTF-8");
            }
            BufferedReader br;
            try {
                br = new BufferedReader(new FileReader(JarToolUtil.getJarDir() + File.separator + "japan.txt"));
                String line;
                StringBuilder newlog = new StringBuilder();
                while (null != (line = br.readLine())) {
                    StringBuilder sb = new StringBuilder();
                    String[] status = line.split("\\s+");
                    if (status.length == 3) { //文件名 状态 时间
                        long last = new File(sourceFolder + File.separator + status[0]).lastModified();
                        //先匹配文件名 当状态是0 或者文件的修改时间晚于记录时间时，则处理文件
                        if (status[1].equals("0") ||
                                LocalDateTime.ofInstant(Instant.ofEpochMilli(last), ZoneId.systemDefault()).isAfter(LocalDateTime.parse(status[2], dtf))) { //只处理状态是 0 的文件
                            //jauas30.160
                            //换名
                            StringBuilder sbn = new StringBuilder();
                            String time = dtf.format(LocalDateTime.now());
                            String yearmonth = time.substring(0, 6); //年月
                            String[] names = status[0].split("\\.");
                            String day = names[1].substring(0, 2);
                            sbn.append(yearmonth).append(day);
                            switch (names[1].substring(2)) {
                                case "0":
                                    sbn.append("00");
                                    break;
                                case "6":
                                    sbn.append("06");
                                    break;
                                case "2":
                                    sbn.append("12");
                                    break;
                                case "8":
                                    sbn.append("18");
                                    break;
                            }
                            DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyyMMddHH");
                            LocalDateTime bj_datetime = LocalDateTime.parse(sbn.toString(), dtf2);
                            bj_datetime = bj_datetime.plusHours(8);
                            String product = "";
                            sbn.delete(0, sbn.length());
                            sbn.append(names[0]).append("_").append(bj_datetime.format(dtf2));
                            sbn.append(".").append(names[1]);
                            time = bj_datetime.format(dtf2);
                            product = names[0].substring(1, 3);
                            File destFile = new File(destFolder + "/" + time.substring(time.length() - 2) + "/" + product + "/" + sbn.toString());
                            try {
                                if (!MainWindow.mainWindow.getOutputTextArea().getText().isEmpty()) {
                                    sb.append(MainWindow.mainWindow.getOutputTextArea().getText()).append(c_string);
                                }
                                if (sb.length() >= 1000) { //清空
                                    MainWindow.mainWindow.getOutputTextArea().setText("");
                                    sb.delete(0, sb.length());
                                }
                                sb.append("[日本]");
                                sb.append("[").append(LocalDateTime.now().toString()).append("]:").append("正在处理文件")
                                        .append(status[0]).append(",转换后文件为").append(destFile.getName()).append(c_string);
                                MainWindow.mainWindow.getOutputTextArea().setText(sb.toString());


                                FileUtil.fileChannelCopy(new File(srcDir + File.separator + status[0]), destFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            sb.append("[").append(LocalDateTime.now().toString()).append("]:").append("文件")
                                    .append(sourceFolder).append("处理完毕");
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
