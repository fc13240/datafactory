package com.moonkin.decode;

import com.moonkin.bean.MiCaps2Data;
import com.moonkin.bean.Station;
import com.moonkin.bean.TK;
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

/**
 * 高空探空报写入mi2文件
 * 高空数据要做时次归并，07,08归到08；19,20归到20；其他时次舍弃
 *
 * @author xuduo
 * @date 2018-09-25
 */
public class HighObservationToMicaps2 {

    private static Logger logger = LoggerFactory.getLogger(HighObservationToMicaps2.class);

    private static Pattern fileNamePattern = Pattern.compile("(UN||US||UX)\\d{6}\\.ABJ", Pattern.CASE_INSENSITIVE);

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    //填充值
    private static String FILL_VALUE = "9999";

    public HighObservationToMicaps2() {
    }

    /**
     * 解析绘图报高空探空数据转为Micap2格式写入文件
     *
     * @param sourceFolder 原报文路径
     * @param targetFolder 生成的Micaps2文件的路径
     */
    public static void writeToMicap2(String sourceFolder, String targetFolder) {
        logger.info("探空报解析任务开始执行");
        String statusFileName = JarToolUtil.getJarDir() + File.separator + "high.txt";
        byte[] b = new byte[2];
        b[0] = 0x0d;
        b[1] = 0x0a;
        String c_string = new String(b);
        HighObservationParseJob hs = new HighObservationParseJob();
        File srcDir = new File(sourceFolder);
        if (!srcDir.exists()) {
            logger.info("高空数据目录不存在，或连接超时");
            return;
        }
        if (srcDir.exists() && srcDir.isDirectory()) {
            File statusFile = new File(statusFileName);
            // 状态文件2小时重写
            if (statusFile.exists()) {
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
//
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
//                            ZoneId.systemDefault()).isBefore(LocalDateTime.now().minusHours(1))){
//                        //读取就旧数据，然后删除新建
//                        boolean flag = statusFile.delete();
//                        StringBuilder logsb = new StringBuilder();
//                        for (String fileName : newlist) {  //文件名 状态 时间戳 初始化时间最早
//                            if(fileNamePattern.matcher(fileName).matches()) {
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
            }

            BufferedReader br;
            try {
                br = new BufferedReader(new FileReader(JarToolUtil.getJarDir() + File.separator + "high.txt"));
                String line;
                StringBuilder sb = new StringBuilder();
                while (null != (line = br.readLine())) {
                    String[] status = line.split("\\s+");
                    if (status.length == 3) { //文件名 状态 时间
                        long last = new File(sourceFolder + File.separator + status[0]).lastModified();
                        //先匹配文件名 当状态是0 或者文件的修改时间晚于记录时间时，则处理文件
                        if (fileNamePattern.matcher(status[0]).matches()) { //只处理状态是 0 的文件
                            logger.info("高空开始处理，当前文件是" + status[0]);
                            List<TK> tkList = hs.getParseData(srcDir + File.separator + status[0]);
                            if (null == tkList || 0 == tkList.size()) {
                                logger.info(status[0] + "无探空数据");
                                FileUtil.modifyFileContent(statusFileName, line, status[0] + " " + "1" + " " + LocalDateTime.ofInstant(Instant.ofEpochMilli(last), ZoneId.systemDefault()).format(dtf));
                                logger.info("状态文件修改完毕，当前状态是" + status[0] + " " + "1" + " " + LocalDateTime.ofInstant(Instant.ofEpochMilli(last), ZoneId.systemDefault()).format(dtf));
                                continue;
                            }
                            //输出日志。。。。。。。。。
                            if (!MainWindow.mainWindow.getOutputTextArea().getText().isEmpty()) {
                                sb.append(MainWindow.mainWindow.getOutputTextArea().getText()).append(c_string);
                            }
                            if (sb.length() >= 1000) { //清空
                                MainWindow.mainWindow.getOutputTextArea().setText("");
                                sb.delete(0, sb.length());
                            }
                            sb.append("[高空]");
                            sb.append("[").append(LocalDateTime.now().toString()).append("]:").append("正在处理文件")
                                    .append(status[0]).append(",转换后文件为").append(targetFolder).append(c_string);
                            MainWindow.mainWindow.getOutputTextArea().setText(sb.toString());

                            writeToMic2(tkList, targetFolder);

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
                logger.error("探空处理异常，" + e.getMessage() + LocalDateTime.now());
            }
        }
    }

    private static void writeToMic2(List<TK> list, String targetFolder) {
        StringBuilder sb = new StringBuilder();
        byte[] b = new byte[2];
        b[0] = 0x0d;
        b[1] = 0x0a;
        String c_string = new String(b);
        String[] heights = {"100", "150", "200", "250", "300", "400", "500", "700", "850", "925", "1000"};
        for (TK t : list) {
            String time = t.getBj_dateTime().substring(0, 10);
            String year = time.substring(2, 4);
            String month = time.substring(4, 6);
            String day = time.substring(6, 8);
            String hour = time.substring(8, 10);
            for (String height : heights) {
                List<MiCaps2Data> result = new ArrayList<>();
                String targetFile = targetFolder + "/" + height + "/" + time + ".000";
                //循环原始数据，提取高度层
                //确定生成文件路径，判断文件是否存在，如果文件存在，则读取数据添加到Micaps2的list里
                File target = new File(targetFile);
                if (target.exists()) { //如果目标文件存在，则把数据读出来
                    List<MiCaps2Data> oldlist = HighObservationParseJob.parseMicap2File(targetFile, "UTF-8");
                    Map<String, List<MiCaps2Data>> olddata = oldlist.stream().collect(groupingBy(MiCaps2Data::getStationId));
                    if (olddata.get(t.getEnStationCode()) == null) { //如果没有当前站点数据
                        MiCaps2Data a = transData(t, height);
                        oldlist.add(a);
                    }
                    result.addAll(oldlist);
                } else {
                    MiCaps2Data a = transData(t, height);
                    result.add(a);
                }
                sb.append("diamond 2 ").append(year).append("年").append(month).append("月").append(day).append("日")
                        .append(hour).append("时").append(height).append("hpa").append("高空观测").append(c_string);
                sb.append(year).append(" ").append(month).append(" ").append(day).append(" ")
                        .append(hour).append(" ");
                sb.append(result.size()).append(c_string);

                //区站号（长整数）  经度  纬度  拔海高度（均为浮点数） 站点级别（整数）   高度  温度
                // 温度露点差  风向  风速（均为浮点数）
                for (MiCaps2Data mi : result) {
                    Station stat = StationInfoLoader.getStationInfoMap().get(mi.getStationId());
                    if (stat != null) {
                        sb.append(mi.getStationId()).append("  ");
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
                        sb.append(fillValue(String.valueOf(mi.getHeight()))).append("  ");
                        sb.append(fillValue(String.valueOf(mi.getT()))).append("  ");
                        sb.append(fillValue(String.valueOf(mi.getTtd()))).append("  ");
                        sb.append(fillValue(String.valueOf(mi.getWindDir()))).append("  ");
                        sb.append(fillValue(String.valueOf(mi.getWindSpeed())));
                        sb.append(c_string);
                    }
                }
                FileUtil.write(targetFile, sb.toString(), "UTF-8");
                //写完本个文件，清空缓存字符串
                sb.delete(0, sb.length());
            }
        }
    }

    private static String fillValue(String value) {
        String result = "";
        if ("null".equals(value) || value == null || value.isEmpty()) {
            result = FILL_VALUE + " ";
        } else {
            result = value + " ";
        }
        return result;
    }

    private static MiCaps2Data transData(TK tk, String height) {
        MiCaps2Data result = new MiCaps2Data();
        result.setStationId(tk.getEnStationCode());
        result.setDatetime(tk.getBj_dateTime());
        String[] datas = new String[5];
        //按高度层分数据
        switch (height) {
            case "100":
                if (tk.getHpa100() != null) {
                    datas = tk.getHpa100().split(";");
                }
                break;
            case "150":
                if (tk.getHpa150() != null) {
                    datas = tk.getHpa150().split(";");
                }
                break;
            case "200":
                if (tk.getHpa200() != null) {
                    datas = tk.getHpa200().split(";");
                }
                break;
            case "250":
                if (tk.getHpa250() != null) {
                    datas = tk.getHpa250().split(";");
                }
                break;
            case "300":
                if (tk.getHpa300() != null) {
                    datas = tk.getHpa300().split(";");
                }
                break;
            case "400":
                if (tk.getHpa400() != null) {
                    datas = tk.getHpa400().split(";");
                }
                break;
            case "500":
                if (tk.getHpa500() != null) {
                    datas = tk.getHpa500().split(";");
                }
                break;
            case "700":
                if (tk.getHpa700() != null) {
                    datas = tk.getHpa700().split(";");
                }
                break;
            case "850":
                if (tk.getHpa850() != null) {
                    datas = tk.getHpa850().split(";");
                }
                break;
            case "925":
                if (tk.getHpa925() != null) {
                    datas = tk.getHpa925().split(";");
                }
                break;
            case "1000":
                if (tk.getHpa1000() != null) {
                    datas = tk.getHpa1000().split(";");
                }
                break;
        }
        if (datas.length > 0 && datas[0] != null && !datas[0].isEmpty()) {
            result.setHeight(Float.valueOf(datas[0]));
        } else {
            result.setHeight(Float.valueOf(FILL_VALUE));
        }
        if (datas.length > 1 && datas[1] != null && !datas[1].isEmpty()) {
            result.setT(Float.valueOf(datas[1]));
        } else {
            result.setT(Float.valueOf(FILL_VALUE));
        }
        if (datas.length > 2 && datas[2] != null && !datas[2].isEmpty()) {
            result.setTtd(Float.valueOf(datas[2]));
        } else {
            result.setTtd(Float.valueOf(FILL_VALUE));
        }
        if (datas.length > 3 && datas[3] != null && !datas[3].isEmpty()) {
            result.setWindDir(Float.valueOf(datas[3]));
        } else {
            result.setWindDir(Float.valueOf(FILL_VALUE));
        }
        if (datas.length > 4 && datas[4] != null && !datas[4].isEmpty()) {
            result.setWindSpeed(Float.valueOf(datas[4]));
        } else {
            result.setWindSpeed(Float.valueOf(FILL_VALUE));
        }

        return result;
    }

    /**
     * 本地测试入口
     */
    public static void main(String[] args) {
        String sourceFile = "\\\\192.168.1.20\\data\\CCAV\\recvdata\\11\\QiXiang\\huitubao\\ux170040.abj";
        String targetFile = "D:\\higiht.000";
        HighObservationToMicaps2.writeToMicap2(sourceFile, targetFile);
    }
}
