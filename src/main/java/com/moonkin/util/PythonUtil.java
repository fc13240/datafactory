package com.moonkin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Python 工具类
 * @author xuduo
 * @since  2018-09-25 08:30:00
 */
public class PythonUtil {

    /**
     *  执行ptyhon文件 无参数传入
     * @param filePath      文件路径
     */
    public static void runPythonFile(String filePath) {
        // 填充参数
        String[] params = new String[2];
        params[0] = "python";
        params[1] = filePath;
        // 传参执行脚本
        try {
            Process progress = Runtime.getRuntime().exec(params);
            progress.waitFor();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(progress.getInputStream()));
            System.out.println("result is : " + bufferedReader.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行Python文件，传入参数
     * @param filePath      文件路径
     * @param args          传入参数列表
     */
    public static void runPythonFile(String filePath, String[] args) {
        // 填充参数
        String[] params = new String[args.length + 2];
        params[0] = JarToolUtil.getJarDir() + "/trans2nc/venv/Scripts/" + "python";
        //params[0] = "D:/数据处理" + "/trans2nc/venv/Scripts/" + "python";
        params[1] = filePath;
        for (int i = 2; i < params.length; i++) {
            params[i] = args[i - 2];
        }
        // 传参执行脚本
        try {
            System.out.println("source is : " + params[2] + params[3]);
            Process progress = Runtime.getRuntime().exec(params);
            //等待跑完
            progress.waitFor();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(progress.getInputStream()));
            System.out.println("result is : " + bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String filepath = "D:/11/ec20180924/";
        String file_name = "echirs_dh24_925_201809240800.168";
        String outpath = "D:/11/temp/";
        runPythonFile(JarToolUtil.getJarDir() + "/trans2nc/read_ec.py",new String[] {filepath,file_name,outpath});
    }
}
