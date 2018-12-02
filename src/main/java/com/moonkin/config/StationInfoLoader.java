package com.moonkin.config;

import com.moonkin.bean.Station;

import java.io.*;
import java.util.*;

/**
 * 站点信息加载
 * @author xuduo
 * @since  2018/9/21
 */
public class StationInfoLoader {

    private static Map<String, Station> stationInfoMap = new HashMap<>();

    static {
        loadFileConfig();
    }

    private static void loadFileConfig() {
        try {
            InputStreamReader in = new InputStreamReader(
                    StationInfoLoader.class.getResourceAsStream("/station_info.txt"), "UTF-8");
            BufferedReader br = new BufferedReader(in);
            String lineString = "";
            int countLine = 1;
            while ((lineString = br.readLine()) != null) {
                if(countLine > 1) { //第二行开始读取站点信息
                    String[] elements = lineString.trim().split("\\t");
                    Station stat = new Station();
                    stat.setStationId(elements[0]);
                    stat.setStationName(elements[1]);
                    stat.setLongitude(Float.valueOf(elements[2]));
                    stat.setLatitude(Float.valueOf(elements[3]));
                    stationInfoMap.put(elements[0], stat);
                }
                //读取配置文件
                countLine++;
            }
    } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Station> getStationInfoMap() {
        return stationInfoMap;
    }

    public static void main(String[] args) {
        StationInfoLoader loader = new StationInfoLoader();
    }
}
