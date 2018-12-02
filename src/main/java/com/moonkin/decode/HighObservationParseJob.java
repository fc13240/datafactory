package com.moonkin.decode;

import com.moonkin.bean.MiCaps2Data;
import com.moonkin.bean.TK;
import com.moonkin.util.ComputeValue;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 高空探空报解析类
 *
 * @author xuduo
 * @date 2018-09-25
 */
public class HighObservationParseJob {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH0000");
    private Calendar local = Calendar.getInstance();
    private StringBuffer sb = new StringBuffer();

    public List<TK> getParseData(String filepath) {
        System.out.print("");
        File file = new File(filepath);
        List datalist = new ArrayList();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String lineString = null;
            String data = null;
            Map map = new HashMap();
            boolean isRead = false;
            String key;
            while ((lineString = reader.readLine()) != null) {
                try {
                    if (lineString.startsWith("TTAA")) {
                        isRead = true;
                    }
                    if (!isRead) {
                        continue;
                    }
                    this.sb.append(lineString).append(" ");
                    if (lineString.contains("=")) {
                        isRead = false;
                        data = this.sb.toString().replaceAll("TTAA|=", "").trim();
                        this.sb.setLength(0);
                        TK jdtk = new TK();
                        parseLineFunc(data, this.local, jdtk);
                        key = jdtk.getBj_dateTime() + ";" + jdtk.getEnStationCode();
                        if (!map.containsKey(key))
                            map.put(key, jdtk);
                    }
                } catch (Exception localException) {
                }
            }
            map.forEach((key1, value) -> {
                if (((TK) value).getEnStationCode() != null) {
                    datalist.add((TK) value);
                }
            });

        } catch (FileNotFoundException localFileNotFoundException) {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException localIOException) {
            }
        } catch (IOException localIOException1) {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException localIOException2) {
            }
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException localIOException3) {
            }
        }
        return datalist;
    }

    private void parseLineFunc(String lineString, Calendar local, TK jdtk)
            throws Exception {
        String[] dataArr = lineString.split("\\s+");

        String level = "99,00,92,85,70,50,40,30,25,20,15,10,88";

        if (dataArr.length < 5) {
            throw new Exception("数据要素缺少，该条记录丢弃！");
        }

        int k = 0;
        for (int i = 0; i < dataArr.length; i++) {
            if (dataArr[i].length() != 5) {
                throw new Exception("存在数据要素长度不等于5，该条记录丢弃！");
            }

            if (dataArr[i].matches("88\\d{3}")) {
                k = i;
            }

        }

        for (int i = 2; i < k; i++) {
            if ((i % 3 == 2) && (!level.contains(dataArr[i].substring(0, 2)))) {
                for (int j = i; j < dataArr.length; j++) {
                    dataArr[j] = null;
                }
                break;
            }
        }

        Calendar cal = Calendar.getInstance();
        cal.add(11, 8);

        int num = 1;
        for (int i = 0; i < dataArr.length; i++) {
            String value = dataArr[i];
            switch (num) {
                case 1: //日期
                    if (value.matches("(([012][0-9])|(3[01]))\\d{3}")) {
                        int day = Integer.parseInt(value.substring(0, 2));
                        if (day > cal.get(5)) {
                            cal.add(2, -1);
                        }
                        cal.set(5, day);
                        cal.set(11, Integer.parseInt(value.substring(2, 4)));
                        cal.add(11, 8);

                        //高空解析要做时次归并，07, 08归到08；19，20归到20
                        int hour = cal.get(Calendar.HOUR_OF_DAY);
                        if (hour == 7 || hour == 19) {
                            cal.add(Calendar.HOUR_OF_DAY, 1);
                        } else if (hour == 8 || hour == 20) {
                        } else {
                            throw new Exception("非08 20，其他时次，舍弃");
                        }
                        jdtk.setBj_dateTime(this.sdf.format(cal.getTime()));
                        num = 2;
                        break;
                    } else {
                        throw new Exception("观测日期异常，该条记录丢弃");
                    }
                case 2: //站号
                    if (value.matches("\\d{5}")) {
                        jdtk.setEnStationCode(value);
                        num = 3;
                        break;
                    } else {
                        throw new Exception("加密站号异常，该条记录丢弃");
                    }
                case 3: //要素开始解析，地面气压/位势高度
                    if (!value.matches("99\\d{3}")) break;
                    if (value.charAt(2) == '0')
                        jdtk.setP("1" + value.substring(2));
                    else {
                        jdtk.setP(value.substring(2));
                    }
                    num = 4;
                    break;
                case 4: //温度，露点温度差
                    String tmp = "";
                    if (value.substring(0, 3).matches("\\d{3}")) {
                        String tmp1 = value.substring(0, 2);
                        String tmp2 = value.substring(2, 3);
                        if (Integer.parseInt(tmp2) % 2 == 1) {
                            tmp = "-" + Integer.parseInt(tmp1) + "." + tmp2;
                        } else {
                            tmp = Integer.parseInt(tmp1) + "." + tmp2;
                        }
                        jdtk.setT(tmp);
                    } else {
                        jdtk.setT("");
                    }
                    //TODO 露点温度，计算规则：>= 56 取 10 * （x - 50）否则 取x，当前值 = (ttt - x) / 10 奇数负偶数正
                    //露点温度差 = 温度 - 露点温度
                    if (value.substring(3, 5).matches("\\d{2}") && value.substring(0, 3).matches("\\d{3}")) {
                        String ttt = value.substring(0, 3);
                        String dd = value.substring(3, 5);
                        float dt = 0.0f; //露点温度
                        int temp = 0;
                        if (Integer.parseInt(dd) >= 56) {
                            temp = Integer.parseInt(ttt) - 10 * (Integer.parseInt(dd) - 50);
                        } else {
                            temp = Integer.parseInt(ttt) - Integer.parseInt(dd);
                        }
                        //dt = temp / 10.0f;
                        if (temp % 2 == 1) {
                            dt = 0 - temp / 10.0f;
                        } else {
                            dt = temp / 10.0f;
                        }
                        if (tmp != null && tmp.length() > 0 && !tmp.isEmpty()) {
                            jdtk.setDp(ComputeValue.transStringPrecision(String.valueOf(Float.valueOf(tmp) - dt), 1));
                        } else {
                            jdtk.setDp("");
                        }
                    } else {
                        jdtk.setDp("");
                    }
                    num = 5;
                    break;
                case 5: //风向，风速
                    if (value.matches("\\d{5}")) {
                        //风向风速解析逻辑修改 20180928 xuduo TODO
                        int wd = Integer.parseInt(value.substring(0, 3));
                        if (wd > 360) {
                            jdtk.setWd("");
                        } else {
                            jdtk.setWd(String.valueOf(wd));
                        }
                        if (wd == 360) {
                            jdtk.setWd("0");
                        }
                        int ws = Integer.parseInt(value.substring(3, 5));
                        jdtk.setWs(String.valueOf(ws));
                    } else {
                        jdtk.setWd("");
                        jdtk.setWs("");
                    }

                    String wstmp = jdtk.getWs() == null ? "" : jdtk.getWs();

                    this.sb.append(jdtk.getP()).append(";").append(jdtk.getT()).append(";").append(jdtk.getDp())
                            .append(";").append(jdtk.getWd()).append(";").append(wstmp);
                    jdtk.setgInfo(this.sb.toString());

                    this.sb.setLength(0);
                    num = 6;
                    break;
                case 6:
                    if ((value.matches("\\d{5}")) && (!value.startsWith("88"))) {
                        String pp = value.substring(0, 2);
                        if (pp.charAt(0) == '0')
                            pp = "10" + pp;
                        else {
                            pp = pp + "0";
                        }
                        jdtk.setHpap(pp);
                        jdtk.setHhh(value.substring(2, 5));
                        String t_tmp = "";
                        i++;
                        if (i < dataArr.length && dataArr[i].substring(0, 3).matches("\\d{3}")) {
                            String tmp1 = dataArr[i].substring(0, 2);
                            String tmp2 = dataArr[i].substring(2, 3);
                            if (Integer.parseInt(tmp2) % 2 == 1) {
                                t_tmp = "-" + Integer.parseInt(tmp1) + "." + tmp2;
                                jdtk.setTt(t_tmp);
                            } else {
                                t_tmp = Integer.parseInt(tmp1) + "." + tmp2;
                                jdtk.setTt(t_tmp);
                            }
                        } else {
                            jdtk.setTt("");
                        }

                        //TODO 露点温度，计算规则：>= 56 取 10 * （x - 50）否则 取x，当前值 = (ttt - x) / 10 奇数负偶数正
                        //露点温度差 = 温度 - 露点温度
                        if (i < dataArr.length && dataArr[i].substring(3, 5).matches("\\d{2}") && dataArr[i].substring(0, 3).matches("\\d{3}")) {
                            String ttt = dataArr[i].substring(0, 3);
                            String dd = dataArr[i].substring(3, 5);
                            float dt = 0.0f; //露点温度
                            int temp = 0;
                            if (Integer.parseInt(dd) >= 56) {
                                temp = Integer.parseInt(ttt) - 10 * (Integer.parseInt(dd) - 50);
                            } else {
                                temp = Integer.parseInt(ttt) - Integer.parseInt(dd);
                            }
                            //dt = temp / 10.0f;
                            if (temp % 2 == 1) {
                                dt = 0 - temp / 10.0f;
                            } else {
                                dt = temp / 10.0f;
                            }
                            if (t_tmp != null && t_tmp.length() > 0 && !t_tmp.isEmpty()) {
                                jdtk.setDd(ComputeValue.transStringPrecision(String.valueOf(Float.valueOf(t_tmp) - dt), 1));
                            } else {
                                jdtk.setDd("");
                            }
                        } else {
                            jdtk.setDd("");
                        }

                        i++;
                        if (i < dataArr.length && dataArr[i].matches("^\\d{5}$")) {
                            if (Integer.parseInt(dataArr[i].substring(2, 3)) >= 5) {
                                jdtk.setDdd(String.valueOf(Integer.parseInt(dataArr[i].substring(0, 2)) * 10 + 5));
                                jdtk.setDds(String.valueOf(Integer.parseInt(dataArr[i].substring(2, 5)) - 500));
                            } else {
                                jdtk.setDdd(String.valueOf(Integer.parseInt(dataArr[i].substring(0, 2)) * 10));
                                jdtk.setDds(String.valueOf(Integer.parseInt(dataArr[i].substring(2, 5))));
                            }
                        } else {
                            jdtk.setDdd("");
                            jdtk.setDds("");
                        }

                        setHpaMessage(jdtk);
                    } else {
                        num = 7;
                    }
                    break;
                case 7:
                    if (value.equals("88999")) {
                        num = 8;
                    } else if (value.matches("88\\d{3}")) {
                        jdtk.setConp(value.substring(2, 5));

                        i++;
                        if (i < dataArr.length && dataArr[i].substring(0, 3).matches("\\d{3}")) {
                            String tmp1 = dataArr[i].substring(0, 2);
                            String tmp2 = dataArr[i].substring(2, 3);
                            if (Integer.parseInt(tmp2) % 2 == 1)
                                jdtk.setCont("-" + Integer.parseInt(tmp1) + "." + tmp2);
                            else
                                jdtk.setCont(Integer.parseInt(tmp1) + "." + tmp2);
                        } else {
                            jdtk.setCont("");
                        }

                        if (i < dataArr.length && dataArr[i].substring(3, 5).matches("\\d{2}"))
                            jdtk.setCondp(dataArr[i].substring(3, 5));
                        else {
                            jdtk.setCondp("");
                        }

                        i++;
                        if (i < dataArr.length && dataArr[i].matches("\\d{5}")) {
                            if (Integer.parseInt(dataArr[i].substring(2, 3)) >= 5) {
                                jdtk.setConwd(String.valueOf(Integer.parseInt(dataArr[i].substring(0, 2)) * 10 + 5));
                                jdtk.setConws(String.valueOf(Integer.parseInt(dataArr[i].substring(2, 5)) - 500));
                            } else {
                                jdtk.setConwd(String.valueOf(Integer.parseInt(dataArr[i].substring(0, 2)) * 10));
                                jdtk.setConws(String.valueOf(Integer.parseInt(dataArr[i].substring(2, 5))));
                            }
                        } else {
                            jdtk.setConwd("");
                            jdtk.setConws("");
                        }

                        this.sb.append(jdtk.getConp()).append(";").append(jdtk.getCont()).append(";").append(jdtk.getCondp())
                                .append(";").append(jdtk.getConwd()).append(";").append(jdtk.getConws());
                        if (jdtk.getLevelcon() != null) {
                            this.sb.insert(0, " ").insert(0, jdtk.getLevelcon());
                        }
                        jdtk.setLevelcon(this.sb.toString());

                        this.sb.setLength(0);
                        num = 7;
                    }
                    break;
                case 8:
                    if (value.equals("77999")) {
                        num = 9;
                    } else if (value.matches("77\\d{3}")) {
                        jdtk.setHwmaxp(value.substring(2, 5));

                        i++;
                        if (i < dataArr.length && dataArr[i].matches("\\d{5}")) {
                            if (Integer.parseInt(dataArr[i].substring(2, 3)) >= 5) {
                                jdtk.setHwwd(String.valueOf(Integer.parseInt(dataArr[i].substring(0, 2)) * 10 + 5));
                                jdtk.setHwws(String.valueOf(Integer.parseInt(dataArr[i].substring(2, 5)) - 500));
                            } else {
                                jdtk.setHwwd(String.valueOf(Integer.parseInt(dataArr[i].substring(0, 2)) * 10));
                                jdtk.setHwws(String.valueOf(Integer.parseInt(dataArr[i].substring(2, 5))));
                            }
                        } else {
                            jdtk.setHwwd("");
                            jdtk.setHwws("");
                        }

                        this.sb.append(jdtk.getHwmaxp()).append(";").append(jdtk.getHwwd())
                                .append(";").append(jdtk.getHwws());
                        if (jdtk.getHw() != null) {
                            this.sb.insert(0, " ").insert(0, jdtk.getHw());
                        }
                        jdtk.setHw(this.sb.toString());

                        this.sb.setLength(0);
                        num = 8;
                    }
                    break;
                case 9:
                    if (value.matches("66\\d{3}")) {
                        jdtk.setNhwmaxp(value.substring(2, 5));

                        i++;
                        if (i < dataArr.length && dataArr[i].matches("\\d{5}")) {
                            if (Integer.parseInt(dataArr[i].substring(2, 3)) >= 5) {
                                jdtk.setNhwwd(String.valueOf(Integer.parseInt(dataArr[i].substring(0, 2)) * 10 + 5));
                                jdtk.setNhwws(String.valueOf(Integer.parseInt(dataArr[i].substring(2, 5)) - 500));
                            } else {
                                jdtk.setNhwwd(String.valueOf(Integer.parseInt(dataArr[i].substring(0, 2)) * 10));
                                jdtk.setNhwws(String.valueOf(Integer.parseInt(dataArr[i].substring(2, 5))));
                            }
                        } else {
                            jdtk.setNhwwd("");
                            jdtk.setNhwws("");
                        }

                        this.sb.append(jdtk.getNhwmaxp()).append(";").append(jdtk.getNhwwd()).append(";").append(jdtk.getNhwws());
                        if (jdtk.getNhw() != null) {
                            this.sb.insert(0, " ").insert(0, jdtk.getNhw());
                        }
                        jdtk.setNhw(this.sb.toString());

                        this.sb.setLength(0);
                        num = 8;
                    }
                    break;
                case 10:
                    if ((value.matches("4\\d{4}")) || (value.equals("31313")))
                        i = dataArr.length;
            }
        }
    }

    private void setHpaMessage(TK jdtk) {
        int p = Integer.parseInt(jdtk.getHpap());

        String levelHpa = "";

        if (p >= 1000) {
            if (!jdtk.getHhh().equals("")) {
                jdtk.setHhh(String.valueOf(Math.round(Double.parseDouble(jdtk
                        .getHhh()) * 0.1D * 100.0D) / 100.0D));
            }
            levelHpa = getLevelHpa(jdtk);
            jdtk.setHpa1000(levelHpa);
        } else if (p >= 900) {
            if (!jdtk.getHhh().equals("")) {
                jdtk.setHhh(String.valueOf(Math.round(Double.parseDouble(jdtk
                        .getHhh()) * 0.1D * 100.0D) / 100.0D));
            }
            levelHpa = getLevelHpa(jdtk);
            jdtk.setHpa925(levelHpa);
        } else if (p >= 800) {
            if (!jdtk.getHhh().equals("")) {
                jdtk.setHhh(String.valueOf(Math.round(
                        (Double.parseDouble(jdtk
                                .getHhh()) + 1000.0D) * 0.1D * 100.0D) / 100.0D));
            }
            levelHpa = getLevelHpa(jdtk);
            jdtk.setHpa850(levelHpa);
        } else if (p >= 700) {
            if (!jdtk.getHhh().equals("")) {
                if (Integer.parseInt(jdtk.getHhh()) >= 500)
                    jdtk.setHhh(String.valueOf(Math.round((
                            Double.parseDouble(jdtk.getHhh()) + 2000.0D) * 0.1D * 100.0D) / 100.0D));
                else {
                    jdtk.setHhh(String.valueOf(Math.round((
                            Double.parseDouble(jdtk.getHhh()) + 3000.0D) * 0.1D * 100.0D) / 100.0D));
                }
            }
            levelHpa = getLevelHpa(jdtk);
            jdtk.setHpa700(levelHpa);
        } else if (p >= 500) {
            levelHpa = getLevelHpa(jdtk);
            jdtk.setHpa500(levelHpa);
        } else if (p >= 400) {
            levelHpa = getLevelHpa(jdtk);
            jdtk.setHpa400(levelHpa);
        } else if (p >= 300) {
            levelHpa = getLevelHpa(jdtk);
            jdtk.setHpa300(levelHpa);
        } else if (p >= 250) {
            if (!jdtk.getHhh().equals("")) {
                jdtk.setHhh(String.valueOf(Integer.parseInt(jdtk.getHhh()) + 1000));
            }
            levelHpa = getLevelHpa(jdtk);
            jdtk.setHpa250(levelHpa);
        } else if (p >= 200) {
            if (!jdtk.getHhh().equals("")) {
                jdtk.setHhh(String.valueOf(Integer.parseInt(jdtk.getHhh()) + 1000));
            }
            levelHpa = getLevelHpa(jdtk);
            jdtk.setHpa200(levelHpa);
        } else if (p >= 150) {
            if (!jdtk.getHhh().equals("")) {
                jdtk.setHhh(String.valueOf(Integer.parseInt(jdtk.getHhh()) + 1000));
            }
            levelHpa = getLevelHpa(jdtk);
            jdtk.setHpa150(levelHpa);
        } else {
            if (!jdtk.getHhh().equals("")) {
                jdtk.setHhh(String.valueOf(Integer.parseInt(jdtk.getHhh()) + 1000));
            }
            levelHpa = getLevelHpa(jdtk);
            jdtk.setHpa100(levelHpa);
        }
    }

    private String getLevelHpa(TK jdtk) {
        String levelHpa = null;
        levelHpa = jdtk.getHhh() + ";" + jdtk.getTt() + ";" + jdtk.getDd() +
                ";" + jdtk.getDdd() + ";" + jdtk.getDds();

        this.sb.setLength(0);
        return levelHpa;
    }

    /**
     * 解析Micaps第二类数据文件
     *
     * @param filePath 文件路径
     * @param encode   文件编码
     * @return
     */
    public static List<MiCaps2Data> parseMicap2File(String filePath, String encode) {
        List<MiCaps2Data> list = new ArrayList<>();
        try {
            if (!new File(filePath).exists()) {
                return null;
            }
            InputStream in = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, encode));
            String lineString = "";
            int countLine = 0;
            while ((lineString = br.readLine()) != null) {
                if (countLine > 1) {
                    MiCaps2Data data = new MiCaps2Data();
                    String[] elements = lineString.trim().split("\\s+");
                    if (elements.length >= 1) {
                        data.setStationId(elements[0]);
                    }
                    if (elements.length >= 2) {
                        data.setLongitude(Float.parseFloat(elements[1]));
                    }
                    if (elements.length >= 3) {
                    data.setLatitude(Float.parseFloat(elements[2]));
                    }
                    if (elements.length >= 4) {
                        data.setSeaheight(Float.parseFloat(elements[3]));
                    }
                    data.setStationLevel(9999);
                    if (elements.length >= 6) {
                        data.setHeight(Float.parseFloat(elements[5]));
                    }
                    if (elements.length >= 7) {
                        data.setT(Float.parseFloat(elements[6]));
                    }
                    if (elements.length >= 8) {
                        data.setTtd(Float.parseFloat(elements[7]));
                    }
                    if (elements.length >= 9) {
                        data.setWindSpeed(Float.parseFloat(elements[9]));
                    }
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

    public static void main(String[] args) {
        HighObservationParseJob h = new HighObservationParseJob();
        List<TK> data = h.getParseData("\\\\192.168.1.20\\data\\CCTV\\recvdata\\11\\QiXiang\\huitubao\\ux170130.abj");
        //List<TK> data = h.getParseData("D:\\test.abj");

        System.out.println(data.size());
    }
}