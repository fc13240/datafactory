package com.moonkin.decode;

import com.moonkin.bean.GroundObservation;
import com.moonkin.util.ComputeValue;

import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 地面绘图报数据解码
 *
 * @author xuduo
 * @date 2018-09-25
 */
public class GroundObservationParseJob {

    private Map<String, String> lchMap = new HashMap<>();

    public List<GroundObservation> getParseData(String filePath) {
        initLchMap();
        System.out.print("");
        List<GroundObservation> dataList = new ArrayList<>();
        File file = new File(filePath);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            // 用于解析的拼接后的完整数据
            StringBuffer parseData = new StringBuffer();
            // 用于判断什么时候开始解码
            boolean isNeed = false;

            DecimalFormat df = new DecimalFormat("0.0");
            ComputeValue cv = new ComputeValue();
            String line = "";
            String time = "";
            while (null != (line = br.readLine())) {
                try {
                    // 解析过程中可能会出项单独行为NIL不处理
                    if ("".equals(line.trim())) {
                        continue;
                    }
                    if (line.contains("NNNN")) {
                        // 如果上面读到的是NNNN就要把ParseData中的数据清空 这里可能用不到
                        parseData.delete(0, parseData.length());
                        // 已经获取需要解析的一组数据，将isNeed置为false重新获取下一组
                        isNeed = false;
                        continue;
                    }
                    if (line.startsWith("AAXX")) {
                        line = line.replaceAll("AAXX", "AAXX ");
                        time = line.split("\\s+")[1];
                        if (null != time && time.substring(0, 4).matches("(0|1|2|3)\\d(0|1|2)\\d")) {
                            isNeed = true;
                        }
                        continue;
                    }
                    // 当读到BBXX的时候，开始拼接想要解析的数据
                    if (isNeed) {
                        // 解析过程中可能会出项单独行为NIL不处理
                        if (line.contains("NIL") || line.contains("--")) {
                            // 可能出现某行已经进入AAXX但在下一行中出现NIL
                            parseData.delete(0, parseData.length());
                            isNeed = false;
                            continue;
                        }
                        parseData.append(line).append(" ");
                        // 直到读到以=结尾时才算一条完整数据拼接完成
                        if (parseData.indexOf("=") > 0) {
                            Calendar cal = Calendar.getInstance();

                            String parse = parseData.toString().replaceAll("=", "");
                            String[] dataArr = parse.split("\\s+");

                            // 用于定位报文位置
                            int num = 1;

                            GroundObservation go = new GroundObservation();

                            if (Integer.parseInt(time.substring(0, 2)) > cal.get(Calendar.DAY_OF_MONTH)) {
                                cal.add(Calendar.MONTH, -1);
                            }

                            // 限定月份为2位
                            String calendarValue = String.valueOf(cal.get(Calendar.MONTH) + 1);
                            if (calendarValue.length() < 2) {
                                calendarValue = "0" + calendarValue;
                            }

                            go.setDateTime(cal.get(Calendar.YEAR) + calendarValue
                                    + time.substring(0, 4));

                            if (LocalDateTime.now().isBefore(LocalDateTime.parse(go.getDateTime(), DateTimeFormatter.ofPattern("yyyyMMddHH")))) {
                                throw new Exception("观测时间大于当前时间，异常丢弃");
                            }

                            // iw――测风方法指示码，
                            // 0、1表示风速以米/秒为单位，3、4表示风速以海里/小时为单位
                            // 如果风向指示码不存在，则默认为米/秒(内定的)
                            if (time.length() == 4 || !judgeString(time.substring(4, 5))) {
                                go.setIw("1");
                            } else {
                                go.setIw(time.substring(4, 5));
                            }

                            for (int i = 0; i < dataArr.length; i++) {
                                String value = dataArr[i];
                                // 如果存在字母，则跳过该元素
                                if (judgeLetter(value)) {
                                    continue;
                                }

                                switch (num) {
                                    case 1: // IIiii 区号 站号
                                        if (value.matches("\\d{5}")) {
                                            go.setStationCode(value);
                                            num = 2;
                                        } else {
                                            num = 19;
                                        }
                                        break;
                                    case 2: // iRiXhVV
                                        if (value.matches("(\\d|/)(\\d|/)(\\d|/)(\\d{2}|//)")) {
                                            if (judgeString(value.substring(1, 2))) {
                                                go.setIx(value.substring(1, 2));
                                            } else {
                                                go.setIx(null);
                                            }
                                            // h 最低云底高度
                                            if (judgeString(value.substring(2, 3))) {
                                                String code = value.substring(2, 3);
                                                String lch = lchMap.get(code);
                                                if (lch != null && !lch.isEmpty()) {
                                                    go.setLch(lch);
                                                } else {
                                                    go.setLch(null);
                                                }
                                            } else {
                                                go.setLch(null);
                                            }
                                            // VV 海面有效能见度
                                            if (judgeString(value.substring(3, 5))) {
                                                go.setVis(value.substring(3, 5));
                                            } else {
                                                go.setVis(null);
                                            }
                                        }
                                        num = 3;
                                        break;
                                    case 3: // Nddff
                                        if (value.matches("(\\d|/)(\\d{4}|////)")) {
                                            // N 总云量
                                            if (judgeString(value.substring(0, 1))) {
                                                go.setTotalCloud(value.substring(0, 1));
                                            } else {
                                                go.setTotalCloud(null);
                                            }
                                            // dd 风向
                                            if (judgeString(value.substring(1, 3))) {
                                                int wd = Integer.parseInt(value.substring(1, 3)) * 10;
                                                if (wd > 360) {
                                                    go.setWd(null);
                                                } else {
                                                    go.setWd(wd + "");
                                                }
                                            } else {
                                                go.setWd(null);
                                            }
                                            // ff――真风速(2位) 不知道单位 要根据IW来做
                                            if (judgeString(value.substring(3, 5))) {
                                                // 如果iw为4则为海里/小时 要转换成 米/秒
                                                if ("4".equals(go.getIw())) {
                                                    go.setWs(df.format(Float.parseFloat(value.substring(3,
                                                            5)) / 1.944) + "");
                                                } else {
                                                    go.setWs(df.format(Float.parseFloat(value.substring(3,
                                                            5))) + "");
                                                }
                                            } else {
                                                go.setWs(null);
                                            }
                                        }
                                        num = 4;
                                        break;
                                    case 4:
                                        if (value.startsWith("1") && 5 == value.length()) {
                                            // Sn――气温正、负号(1代表正，0代表负) 1SnTTT
                                            String Tsn = value.substring(1, 2);

                                            if (judgeString(value.substring(2, 5))) {
                                                if ("1".equals(Tsn)) {
                                                    // TTT――气温，编报单位0.1℃。(3位)
                                                    go.setT("-" + (Float.parseFloat(value.substring(2, 5)) / 10));
                                                } else {
                                                    go.setT((Float.parseFloat(value.substring(2, 5)) / 10) + "");
                                                }
                                            } else {
                                                go.setT(null);
                                            }
                                            num = 5;
                                            break;
                                        }

                                    case 5:
                                        // 2snTdTdTd
                                        if (value.startsWith("2") && 5 == value.length()) {
                                            // 2SnTdTdTd 2露点组指示码。 TdTdTd 露点温度。（
                                            // 露点可能数据中没有）
                                            if (!("".equals(value)) && judgeString(value)) {
                                                // 判断温度的正负号，正值或0编“0”，负值编“1”。
                                                String dewSn = value.substring(1, 2);

                                                // 为9的时候表示相对湿度
                                                if ("9".equals(dewSn)) {
                                                    go.setRh(value.substring(2, 5));
                                                    go.setDewt(cv.computeTD(go.getRh(), go.getT()));
                                                } else {
                                                    if ("1".equals(dewSn)) {
                                                        go.setDewt("-" + (Float.parseFloat(value.substring(2, 5)) / 10));
                                                    } else {
                                                        go.setDewt(Float.parseFloat(value.substring(2, 5)) / 10 + "");
                                                    }
                                                    go.setRh(cv.computeRH(go.getT(), go.getDewt()));
                                                }
                                                if (null != go.getRh()) {
                                                    int rh = Integer.parseInt(go.getRh());
                                                    if (100 < rh) {
                                                        go.setRh("100");
                                                    }
                                                }
                                            }
                                            num = 6;
                                            break;
                                        }

                                    case 6:
                                        // 3PPPP
                                        if (value.startsWith("3") && !value.startsWith("333") && 5 == value.length()) {
                                            if (judgeString(value.substring(1, 5))) {
                                                if (Integer.parseInt(value.substring(1, 2)) > 5) {
                                                    // 4――气压，编报单位0.1hPa;(4位)
                                                    go.setP((Float.parseFloat(value.substring(1, 5)) / 10) + "");
                                                } else {
                                                    // 4――气压，编报单位0.1hPa;(4位)
                                                    go.setP((Float.parseFloat("1"
                                                            + value.substring(1, 5)) / 10)
                                                            + "");
                                                }
                                            } else {
                                                go.setP(null);
                                            }
                                            num = 7;
                                            break;
                                        }

                                    case 7:
                                        // 4PPPP
                                        if (value.startsWith("4") && 5 == value.length()) {
                                            if (judgeString(value.substring(1, 5))) {
                                                int second = Integer.parseInt(value.substring(1, 2));
                                                if (second != 1 && second != 2 && second != 5 && second != 7 && second != 8) {
                                                    if (Integer.parseInt(value.substring(1, 2)) > 5) { // 保留3位，不做转换，客户端专用，20180925
                                                        // 4――海平面气压，编报单位0.1hPa;(4位)
                                                        go.setSeaP((Float.parseFloat(value.substring(1, 5)) / 10)
                                                                + "");
//													go.setSeaP((Integer.parseInt(value.substring(1, 5)) + ""));
                                                    } else {
                                                        // 4――海平面气压，编报单位0.1hPa;(4位)
                                                        go.setSeaP((Float.parseFloat("1"
                                                                + value.substring(1, 5)) / 10)
                                                                + "");
//													go.setSeaP((Integer.parseInt(value.substring(1, 5)) + ""));
                                                    }
                                                } else {
                                                    go.setSeaP(null);
                                                }
                                            } else {
                                                go.setSeaP(null);
                                            }
                                            num = 8;
                                            break;
                                        }

                                    case 8:
                                        // 5aPPP
                                        if (value.startsWith("5") && !value.startsWith("555") && 5 == value.length()) {
                                            // 过去3小时气压变化倾向
                                            int trend = 0;
                                            if (judgeString(value.substring(1, 2))) {
                                                go.setTrend(value.substring(1, 2));
                                                trend = Integer.parseInt(value.substring(1, 2));
                                            } else {
                                                go.setTrend(null);
                                            }

                                            if (judgeString(value.substring(2))) {
                                                // 4――3小时变压，编报单位0.1hPa;(4位)
                                                if (trend > 4) {
                                                    go.setP3h("-" + (Float.parseFloat(value.substring(2, 5)) / 10) + "");
                                                } else {
                                                    go.setP3h((Float.parseFloat(value.substring(2, 5)) / 10) + "");
                                                }

                                            } else {
                                                go.setP3h(null);
                                            }

                                            num = 9;
                                            break;
                                        }

                                    case 9:
                                        // 6RRR1
                                        if (value.startsWith("6") && 5 == value.length()) {
                                            if (judgeString(value.substring(1, 4))) {
                                                go.setRain(value.substring(1, 4));
                                            } else {
                                                go.setRain(null);
                                            }

                                            num = 10;
                                            break;
                                        }

                                    case 10:
                                        // 7wwW1W2
                                        if (value.startsWith("7") && 5 == value.length()) {
                                            // ww 现在天气
                                            if (judgeString(value.substring(1, 3))) {
                                                go.setWw(value.substring(1, 3));
                                            } else {
                                                go.setWw(null);
                                            }

                                            // W1W2 过去天气
                                            if (judgeString(value.substring(3, 4))) {
                                                go.setCwlw1(value.substring(3, 4));
                                            } else {
                                                go.setCwlw1(null);
                                            }
                                            // W1W2 过去天气
                                            if (judgeString(value.substring(4, 5))) {
                                                go.setCwlw2(value.substring(4, 5));
                                            } else {
                                                go.setCwlw2(null);
                                            }

                                            num = 11;
                                            break;
                                        }

                                    case 11:
                                        // 8NhCLCMCH
                                        if (value.startsWith("8") && 5 == value.length()) {
                                            // Nh 低云量
                                            if (judgeString(value.substring(1, 2))) {
                                                go.setLcl(value.substring(1, 2));
                                            } else {
                                                go.setLcl(null);
                                            }

                                            // CL 低云状
                                            if (judgeString(value.substring(2, 3))) {
                                                go.setLcs(value.substring(2, 3));
                                            } else {
                                                go.setLcs(null);
                                            }

                                            // CM 中云状
                                            if (judgeString(value.substring(3, 4))) {
                                                go.setMcs(value.substring(3, 4));
                                            } else {
                                                go.setMcs(null);
                                            }

                                            // CH 高云状
                                            if (judgeString(value.substring(4))) {
                                                go.setHcs(value.substring(4));
                                            } else {
                                                go.setHcs(null);
                                            }
                                            num = 12;
                                            break;
                                        }
                                    case 12:
                                        // 用于指示下面要解得必须为3段指示码开始
                                        if (value.startsWith("333")) {
                                            num = 13;
                                            break;
                                        }
                                    case 13:
                                        // 0P24P24T24T24
                                        if (value.startsWith("0") && 5 == value.length()) {

                                            // P24P24 过去24小时变压
                                            if (judgeString(value.substring(1, 3))
                                                    && value.substring(1, 3).matches("\\d\\d")) {
                                                go.setP24h((Float.parseFloat(value.substring(1, 3)) / 10) + "");
                                            } else {
                                                go.setP24h(null);
                                            }

                                            // T24T24 过去24小时变温
                                            if (judgeString(value.substring(3))) {
                                                go.setT24h((Float.parseFloat(value.substring(3)) / 10)
                                                        + "");
                                            } else {
                                                go.setT24h(null);
                                            }

                                            num = 14;
                                            break;
                                        }

                                    case 14:
                                        // 1snTXTXTX
                                        if (value.startsWith("1") && 5 == value.length()) {
                                            if (judgeString(value.substring(1, 2))) {
                                                if ("0".equals(value.substring(1, 2))) {
                                                    if (judgeString(value.substring(2))) {
                                                        go.setT24hMax((Float.parseFloat(value.substring(2)) / 10) + "");
                                                    } else {
                                                        go.setT24hMax(null);
                                                    }
                                                } else {
                                                    if (judgeString(value.substring(2))) {
                                                        go.setT24hMax("-" + (Float.parseFloat(value.substring(2)) / 10));
                                                    } else {
                                                        go.setT24hMax(null);
                                                    }
                                                }
                                            } else {
                                                // sn为斜杠时 数据不录入
                                                go.setT24hMax(null);
                                            }

                                            num = 15;
                                            break;
                                        }

                                    case 15: // 2snTnTnTn
                                        if (value.startsWith("2") && 5 == value.length()) {
                                            if (judgeString(value.substring(1, 2))) {
                                                if ("0".equals(value.substring(1, 2))) {
                                                    if (judgeString(value.substring(2))) {
                                                        go.setT24hMin((Float.parseFloat(value
                                                                .substring(2)) / 10) + "");
                                                    } else {
                                                        go.setT24hMin(null);
                                                    }
                                                } else {
                                                    if (judgeString(value.substring(2))) {
                                                        go.setT24hMin("-"
                                                                + (Float.parseFloat(value.substring(2)) / 10));
                                                    } else {
                                                        go.setT24hMin(null);
                                                    }
                                                }
                                            } else {
                                                // sn为斜杠时 数据不录入
                                                go.setT24hMin(null);
                                            }

                                            num = 16;
                                            break;
                                        }

                                    case 16: // 3snTgTgTg
                                        if (value.startsWith("3") && 5 == value.length()) {
                                            if (judgeString(value.substring(1, 2))) {
                                                if ("0".equals(value.substring(1, 2))) {
                                                    if (judgeString(value.substring(2))) {
                                                        go.settMin((Float.parseFloat(value.substring(2)) / 10)
                                                                + "");
                                                    } else {
                                                        go.settMin(null);
                                                    }
                                                } else {
                                                    if (judgeString(value.substring(2))) {
                                                        go.settMin("-"
                                                                + (Float.parseFloat(value.substring(2)) / 10));
                                                    } else {
                                                        go.settMin(null);
                                                    }
                                                }
                                            } else {
                                                // sn为斜杠时 数据不录入
                                                go.settMin(null);
                                            }
                                            num = 17;
                                            break;
                                        }

                                    case 17:
                                        if (value.startsWith("7") && 5 == value.length()) {
                                            if (judgeString(value.substring(1))) {
                                                go.setRain24h((Float.parseFloat(value.substring(1)) / 10) + "");
                                            } else {
                                                go.setRain24h(null);
                                            }
                                            num = 18;
                                            break;
                                        }
                                    case 18:
                                        // 可能会出现555，之后的数据是不需要解析的
                                        if (value.startsWith("555")) {
                                            num = 19;
                                            break;
                                        }
                                    case 19:
                                        break;

                                    default:

                                }
                            }

                            if (null != go.getStationCode()) {
                                dataList.add(go);
                            }

                            // 处理完本条记录时，需将本条记录清除，继续下一条记录
                            parseData.setLength(0);
                        }
                    }
                } catch (Exception e) {
                    // 出现异常时，需将parseData中的本条记录清除，继续下一条记录
                    parseData.setLength(0);
                    continue;
                }
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
        return dataList;
    }

    /**
     * 判断字符串里是否有"/"
     *
     * @param str
     * @return
     */
    public boolean judgeString(String str) {
        boolean flag = true;
        String[] temp = str.split("");
        for (int i = 0; i < temp.length; i++) {
            if ("-".equals(temp[i].trim()) || "/".equals(temp[i].trim())) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 判断字符串里是否有字母
     *
     * @param str
     * @param str
     * @return
     */
    public static boolean judgeLetter(String str) {
        boolean flag = false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isLetter(str.charAt(i))) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 解析Micaps第一类数据文件
     *
     * @param filePath 文件路径
     * @param encode   文件编码
     * @return
     */
    public static List<GroundObservation> parseMicap1File(String filePath, String encode) {
        List<GroundObservation> list = new ArrayList<>();
        try {
            if (!new File(filePath).exists()) {
                return null;
            }
            InputStream in = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, encode));
            String lineString = "";
            String timeString = "";
            int countLine = 1;
            while ((lineString = br.readLine()) != null) {
                if (countLine == 1) {
                    timeString = br.readLine(); //读取第二行
                    String[] times = timeString.trim().split("\\s+");
                    timeString = "20" + times[0] + times[1] + times[2] + times[3];
                } else {
                    lineString += br.readLine(); //一次读两行
                }
                if (countLine > 1) { //从第3行开始解析数据
                    GroundObservation data = new GroundObservation();
                    String[] elements = lineString.trim().split("\\s+");
                    data.setStationCode(elements[0]);
                    //data.setLongitude(Float.parseFloat(elements[1]));
                    //data.setLatitude(Float.parseFloat(elements[2]));
                    //data.setSeaheight(Float.parseFloat(elements[3]));
                    //data.setStationLevel(Integer.parseInt(elements[4]));
                    data.setDateTime(timeString);
                    data.setTotalCloud(elements[5]);
                    data.setWd(elements[6]);
                    data.setWs(elements[7]);
                    data.setSeaP(elements[8]);
                    data.setP3h(elements[9]);
                    data.setCwlw1(elements[10]);
                    data.setCwlw2(elements[11]);
                    //data.setRainfall6Hour(Float.parseFloat(elements[12]));
                    data.setLcs(elements[13]);
                    data.setLcl(elements[14]);
                    data.setLch(elements[15]);
                    data.setDewt(elements[16]);
                    data.setVis(elements[17]);
                    data.setWw(elements[18]);
                    data.setT(elements[19]);
                    data.setMcs(elements[20]);
                    data.setHcs(elements[21]);
                    data.setT24h(elements[24]);
                    data.setP24h(elements[25]);
                    list.add(data);
                }
                countLine++;
            }
            br.close();
            in.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initLchMap() {
        lchMap.put("0", "0");
        lchMap.put("1", "50");
        lchMap.put("2", "100");
        lchMap.put("3", "200");
        lchMap.put("4", "300");
        lchMap.put("5", "600");
        lchMap.put("6", "1000");
        lchMap.put("7", "1500");
        lchMap.put("8", "2000");
        lchMap.put("9", "2500");
    }

    /**
     * 测试入口
     *
     * @param args
     */

    public static void main(String[] args) {

        String filePath = "\\\\192.168.1.20\\data\\CCTV\\recvdata\\11\\QiXiang\\huitubao\\sx100920.abj";
        GroundObservationParseJob parseJob = new GroundObservationParseJob();
        List<GroundObservation> list = parseJob.getParseData(filePath);
        System.out.println();

    }

}
