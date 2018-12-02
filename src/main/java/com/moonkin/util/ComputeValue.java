package com.moonkin.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ComputeValue {
	/*
	 * 根据温度与露点温度计算相对温度
	 */
	public String computeRH(String ts, String tds){
		if (null == ts || null == tds){
			return null;
		}
		float t = Float.parseFloat(ts);
		float td = Float.parseFloat(tds);
		double m = (ee(td) / ee(t)) * 100;
		BigDecimal bd = new BigDecimal(m).setScale(0, BigDecimal.ROUND_HALF_UP);
	    return bd.toString();
		
	}	
	
	private static double ee(float t){		
		return (6.112 * (Math.exp((17.678 * t)/(243.5 + t))));
	}
	
	/*
	 * 根据温度与相对温度计算露点温度
	 */
	public String computeTD(String rhs, String ts){
		if (null == ts || null == rhs){
			return null;
		}
		float t = Float.parseFloat(ts);
		float rh = Float.parseFloat(rhs);
		double m = Math.exp((17.678 * t) / (t + 243.5));
		m = Math.log(rh / 100 * m);
		m = (m * 243.5) / (17.678 - m);
		BigDecimal bd = new BigDecimal(m).setScale(1, BigDecimal.ROUND_HALF_UP);
	    return bd.toString();

	}
	
	/**
	 * 比较时间，如果文件时间比系统当前时间大则返回false，否则返回true
	 * @param fileTime 文件时间
	 * @return
	 */
	public boolean compareTime(String fileTime) {
		
		if(fileTime == null || fileTime.length() < 10) {
			return false;
		}else if(fileTime.length() == 10){ // 补上分秒
			fileTime += "0000";
		}else if(fileTime.length() == 12){ // 补上秒
			fileTime += "00";
		}
		
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(fileTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long fileMillis = cal.getTimeInMillis();
		long curMillis = System.currentTimeMillis();
		if(fileMillis > curMillis) { // 如果文件时间大于当前系统时间，则无效
			return false;
		}
		return true;
	}
	
	/*
	 * 把云量由百分比转为成
	 */
	public String computeCloud(String cloud){
		int c = Integer.parseInt(cloud);
		String value = "";
		if (c < 10){
			value = "0";
		}else if (c < 20){
			value = "1";
		}else if (c < 30){
			value = "2";
		}else if (c < 40){
			value = "3";
		}else if (c < 50){
			value = "4";
		}else if (c < 60){
			value = "5";
		}else if (c < 70){
			value = "6";
		}else if (c < 80){
			value = "7";
		}else if (c < 90){
			value = "8";
		}else if (c < 100){
			value = "9";
		}else{
			value = "10";
		}
		return value;
	}
	
	
	/**
	 * 判断数据是否存在"."，存在返回0
	 * @param s
	 * @return
	 */
	public int IsPoint(String s){
		boolean flag=false;
		int ele=0;
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)!='.'){
				flag=true;
				break;}
		}
		if(flag){
			ele=Integer.parseInt(s);
		}
		return ele;
	}
	/**
	 * 计算数据乘0.1
	 * @param 
	 * @return
	 */
	public float Calculate(String s){
		boolean flag=false;
		float ele=0;
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)!='.'){
				flag=true;
				break;}
		}
		if(flag){
			 ele= (float) (Integer.parseInt(s) * 0.1);
		}
		return ele;
		
	}

	/**
	 * 把单个字符串数字转换精度
	 *
	 * @param num
	 *            字符串数字 ---59.87676
	 * @param scale
	 *            小数点后几位 0,1,2...
	 * @return 保留精度后的字符串
	 */
	public static String transStringPrecision(String num, int scale)
	{
		String result = "";// 保存结果
		try
		{
			if ("".equals(num) || num == null)
			{
				return "";
			}
			float number = Float.parseFloat(num);

			if (number == 999.9f || number == -999.9f)
				return "";
			BigDecimal b = new BigDecimal(number);
			float f1;
			int n1;
			if (scale != 0)
			{
				f1 = b.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();// 四舍五入
				result = String.valueOf(f1);
			} else
			{
				n1 = b.setScale(scale, BigDecimal.ROUND_HALF_UP).intValue();
				result = String.valueOf(n1);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String t = new ComputeValue().computeRH("-6.3", "-17.4");
		System.out.println(t);
	}

}
