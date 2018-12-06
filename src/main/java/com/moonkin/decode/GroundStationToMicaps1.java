package com.moonkin.decode;

import com.moonkin.bean.GroundObservation;
import com.moonkin.bean.Station;
import com.moonkin.config.StationInfoLoader;
import com.moonkin.ui.MainWindow;
import com.moonkin.util.FileUtil;
import com.moonkin.util.JarToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.groupingBy;

public class GroundStationToMicaps1 {

    private static Logger logger = LoggerFactory.getLogger(GroundStationToMicaps1.class);

    private static Pattern fileNamePattern = Pattern.compile("(SX|SN|SS|SH|XX|WX|QT|DB)\\d{6}\\.ABJ", Pattern.CASE_INSENSITIVE);

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    //填充值
    private static String FILL_VALUE = "9999";

    public GroundStationToMicaps1() {
    }

    /**
     * 解析地面绘图报数据转为Micap1格式写入文件
     *
     * @param sourceFolder 原报文目录
     * @param targetFolder 生成的Micaps1文件的目录
     */
    public static void writeToMicap1(String sourceFolder, String targetFolder) {
        String statusFileName = JarToolUtil.getJarDir() + File.separator + "ground.txt";
        byte[] b = new byte[2];
        b[0] = 0x0d;
        b[1] = 0x0a;
        String c_string = new String(b);
        GroundObservationParseJob gs = new GroundObservationParseJob();
        File srcDir = new File(sourceFolder);
        if (!srcDir.exists()) {
            logger.info("地面数据目录不存在，或连接超时");
            return;
        }
        if (srcDir.exists() && srcDir.isDirectory()) {
            File statusFile = new File(statusFileName);
            // 状态文件2小时重写
            if(statusFile.exists()) {
                statusFile.delete();
//                String line;
//                BufferedReader br;
//                try {
//                    br = new BufferedReader(new FileReader(statusFileName));
//                    List<String> oldlist = new ArrayList<>();
//                    //读取原状态文件内容，再把旧文件去掉
//                    while (null != (line = br.readLine())) {
//                        String[] status = line.split("\\s+");
//                        if (status[1].equals("1")) {
//                            oldlist.add(status[0]);
//                        }
//                    }
//                    br.close();
//                    String[] fileNames = srcDir.list();
//                    fileNames = Arrays.stream(fileNames).sorted((f1,f2) -> {
//                        if(f1.substring(2).compareTo(f2.substring(2)) > 0) {
//                            return -1;
//                        } else {
//                            return 1;
//                        }
//                    }).toArray(String[]::new);
//                    ArrayList<String> newlist = new ArrayList<>(Arrays.asList(fileNames));
//                    newlist.removeAll(oldlist);
//                    //全部处理完或者超过1小时没更新
//                    if((newlist.size() > 0) || LocalDateTime.ofInstant(Instant.ofEpochMilli(statusFile.lastModified()),
//                            ZoneId.systemDefault()).isBefore(LocalDateTime.now().minusHours(1))) {
//                        //读取就旧数据，然后删除新建
//                        boolean flag = statusFile.delete();
//                        StringBuilder logsb = new StringBuilder();
//                        for (String fileName : newlist) {  //文件名 状态 时间戳 初始化时间最早
//                            if (fileNamePattern.matcher(fileName).matches()) {
//                                logsb.append(fileName).append(" ").append("0").append(" ").append(LocalDateTime.now().minusHours(3).format(dtf)).append(c_string);
//                            }
//                        }
//                        FileUtil.write(statusFileName, logsb.toString(), "UTF-8");
//                    }
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e2) {
//                    e2.printStackTrace();
//                }
            }
            //文件不存在创建状态记录文件
            if (!statusFile.exists()) {
                logger.info("地面处理记录文件初始化");
                System.out.println("正在初始化状态文件 " + statusFileName + "... ...");
                String[] fileNames = srcDir.list();
                fileNames = Arrays.stream(fileNames).sorted((f1,f2) -> {
                    if(f1.substring(2).compareTo(f2.substring(2)) > 0) {
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
                logger.info("地面处理记录文件初始化创建完毕");
            }
            BufferedReader br;
            try {
                br = new BufferedReader(new FileReader(JarToolUtil.getJarDir() + File.separator + "ground.txt"));
                String line;
                StringBuilder sb = new StringBuilder();
                while (null != (line = br.readLine())) {
                    String[] status = line.split("\\s+");
                    if (status.length == 3) { //文件名 状态 时间
                        long last = new File(sourceFolder + File.separator + status[0]).lastModified();
                        //先匹配文件名 当状态是0 或者文件的修改时间晚于记录时间时，则处理文件
                        if (fileNamePattern.matcher(status[0]).matches()) { //只处理状态是 0 的文件
                            logger.info("地面开始处理，当前文件是" + status[0]);
                            List<GroundObservation> groundStationList = gs.getParseData(sourceFolder + File.separator + status[0]);
                            if (null == groundStationList || 0 == groundStationList.size()) {
                                logger.info(status[0] + "无地面数据");
                                FileUtil.modifyFileContent(statusFileName, line, status[0] + " " + "1" + " " + LocalDateTime.ofInstant(Instant.ofEpochMilli(last), ZoneId.systemDefault()).format(dtf));
                                logger.info("状态文件修改完毕，当前状态是" + status[0] + " " + "1" + " " + LocalDateTime.ofInstant(Instant.ofEpochMilli(last), ZoneId.systemDefault()).format(dtf));
                                continue;
                            }
                            DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyyMMddHH");
                            String time = groundStationList.get(0).getDateTime();
                            LocalDateTime ldt = LocalDateTime.parse(time, dtf2);
                            //加8小时
                            ldt = ldt.plusHours(8);
                            //目标路径：跟路径 + 文件名
                            String targetFile = targetFolder + "/" + ldt.format(dtf2) + ".000";
                            //2 5 8 11 14 17 20 23
                            if (ldt.getHour() % 3 == 2) { //只出这几个时间的
                                File target = new File(targetFile);
                                if (target.exists()) { //如果目标文件存在，则把数据读出来
                                    List<GroundObservation> result = new ArrayList<>();
                                    List<GroundObservation> list = GroundObservationParseJob.parseMicap1File(targetFile, "UTF-8");
                                    Map<String, List<GroundObservation>> olddata = list.stream().collect(groupingBy(GroundObservation::getStationCode));
                                    Map<String, List<GroundObservation>> newdata = groundStationList.stream().collect(groupingBy(GroundObservation::getStationCode));
                                    //数据去重复!!TODO
                                    newdata.putAll(olddata);
                                    for (String key : newdata.keySet()) {
                                        result.addAll(newdata.get(key));
                                    }
                                    groundStationList = result;
                                }
                                //输出日志。。。。。。。。。
                                if (!MainWindow.mainWindow.getOutputTextArea().getText().isEmpty()) {
                                    sb.append(MainWindow.mainWindow.getOutputTextArea().getText()).append(c_string);
                                }
                                if (sb.length() >= 1000) { //清空
                                    MainWindow.mainWindow.getOutputTextArea().setText("");
                                    sb.delete(0, sb.length());
                                }
                                sb.append("[地面]");
                                sb.append("[").append(LocalDateTime.now().toString()).append("]:").append("正在处理文件")
                                        .append(status[0]).append(",转换后文件为").append(targetFile).append(c_string);
                                MainWindow.mainWindow.getOutputTextArea().setText(sb.toString());
                                //写入文件
                                writeToMic1(groundStationList, targetFile);
                                logger.info("地面文件处理完毕，当前文件是" + status[0]);
                                sb.append("[").append(LocalDateTime.now().toString()).append("]:").append("文件")
                                        .append(status[0]).append("处理完毕").append(c_string);
                                MainWindow.mainWindow.getOutputTextArea().setText(sb.toString());
                                //修改对应的记录行
                                FileUtil.modifyFileContent(statusFileName, line, status[0] + " " + "1" + " " + LocalDateTime.ofInstant(Instant.ofEpochMilli(last), ZoneId.systemDefault()).format(dtf));
                                logger.info("状态文件修改完毕，当前状态是" + status[0] + " " + "1" + " " + LocalDateTime.ofInstant(Instant.ofEpochMilli(last), ZoneId.systemDefault()).format(dtf));
                            }
                        }
                    }
                    //更新记录文件
//                    FileUtil.write(statusFileName, newlog.toString(), "UTF-8");
//                    System.out.println("记录文件写入完毕");
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("地面处理异常 ：" + e.getMessage());
            }
        }
    }

    private static void writeToMic1(List<GroundObservation> list, String targetFile) {
        StringBuilder sb = new StringBuilder();
        byte[] b = new byte[2];
        b[0] = 0x0d;
        b[1] = 0x0a;
        String c_string = new String(b);

        if (list != null && list.size() > 0) {
            String time = list.get(0).getDateTime();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHH");
            LocalDateTime ldt = LocalDateTime.parse(time, dtf);
            //加8小时
            ldt = ldt.plusHours(8);
            time = ldt.format(dtf);
            String year = time.substring(2, 4);
            String month = time.substring(4, 6);
            String day = time.substring(6, 8);
            String hour = time.substring(8, 10);
            sb.append("diamond 1 ").append(year).append("年").append(month).append("月").append(day).append("日")
                    .append(hour).append("时").append("地面观测").append(c_string);
            sb.append(year).append(" ").append(month).append(" ").append(day).append(" ")
                    .append(hour).append(" ").append(list.size()).append(c_string);
        }

        for (GroundObservation g : list) {
            //区站号（长整数）  经度  纬度  拔海高度（均为浮点数）站点级别（整数）  总云量  风向  风速
            // 海平面气压（或本站气压）  3小时变压  过去天气1  过去天气2  6小时降水 低云状  低云量  低云高
            // 露点  能见度  现在天气  温度 中云状  高云状  标志1  标志2（均为整数） 24小时变温  24小时变压
            sb.append(g.getStationCode()).append("  ");
            Station stat = StationInfoLoader.getStationInfoMap().get(g.getStationCode());
            if (stat != null) {
                if (stat.getLongitude() != null) {
                    sb.append(stat.getLongitude()).append("  ");
                } else {
                    sb.append(FILL_VALUE).append("  ");
                }
                if (stat.getLongitude() != null) {
                    sb.append(stat.getLatitude()).append("  ");
                } else {
                    sb.append(FILL_VALUE).append("  ");
                }
                if (stat.getSeaheight() != null) {
                    sb.append(stat.getSeaheight()).append("  ");
                } else {
                    sb.append(FILL_VALUE).append("  ");
                }
                if (stat.getStationLevel() != null) {
                    sb.append(stat.getStationLevel()).append("  ");
                } else {
                    sb.append(FILL_VALUE).append("  ");
                }
            } else {
                sb.append(FILL_VALUE).append("  ");
                sb.append(FILL_VALUE).append("  ");
                sb.append(FILL_VALUE).append("  ");
                sb.append(FILL_VALUE).append("  ");
            }
            sb.append(fillValue(g.getTotalCloud()));
            sb.append(fillValue(g.getWd()));
            sb.append(fillValue(g.getWs()));
            sb.append(fillValue(g.getSeaP()));
            sb.append(fillValue(g.getP3h()));
            sb.append(fillValue(g.getCwlw1()));
            sb.append(fillValue(g.getCwlw2()));
            sb.append(c_string);
            sb.append("   " + FILL_VALUE + " "); //6小时降水
            sb.append(fillValue(g.getLcs()));
            sb.append(fillValue(g.getLcl()));
            sb.append(fillValue(g.getLch()));
            sb.append(fillValue(g.getDewt()));
            sb.append(fillValue(g.getVis()));
            sb.append(fillValue(g.getWw()));
            sb.append(fillValue(g.getT()));
            sb.append(fillValue(g.getMcs()));
            sb.append(fillValue(g.getHcs()));
            sb.append("   " + FILL_VALUE + " "); //Flag1
            sb.append("   " + FILL_VALUE + " "); //Flag2
            sb.append(fillValue(g.getT24h()));
            sb.append(fillValue(g.getP24h()));
            sb.append(c_string);
        }
        FileUtil.write(targetFile, sb.toString(), "UTF-8");
    }

    private static String fillValue(String value) {
        String result;
        if (value == null || value.isEmpty()) {
            result = FILL_VALUE + " ";
        } else {
            result = value + " ";
        }
        return result;
    }


    /**
     * 本地测试入口
     */
    public static void main(String[] args) {
        String sourceFile = "D:\\huitubao";
        String targetFile = "D:\\micaps1";
        GroundStationToMicaps1.writeToMicap1(sourceFile, targetFile);
    }
}
