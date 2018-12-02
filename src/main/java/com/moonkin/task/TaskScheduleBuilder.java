package com.moonkin.task;

import org.quartz.CronScheduleBuilder;
import org.quartz.SimpleScheduleBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时任务调度
 * 
 * @author 张弛
 * 
 */
public class TaskScheduleBuilder {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	private String second = "0";

	private String minute = "0";

	private String hour = "0";

	private String day = "*";

	private String month = "*";

	private String week = "?";

	private String year = "";

	private TaskScheduleBuilder() {
		super();
	}

	/**
	 * 获得一个任务周期，不可以直接使用，需要设置时间：秒、分钟、小时、天、月、星期、年等，<br>
	 * 最后调用getSchedule来获取相应的表达式来使用
	 * 
	 * @return TaskScheduleBuilder
	 */
	public static TaskScheduleBuilder getInstance() {
		return new TaskScheduleBuilder();
	}

    /**
     * 按照cron时间串触发任务
     * @param cron 0 30 * * * ?
     * @author xuduo
     * @return
     */
	public static Object getCronSchedule(String cron) {
        return CronScheduleBuilder.cronSchedule(cron);
	}

	/**
	 * 获取一个按小时的任务周期，可直接使用
	 * 
	 * @param hours
	 *            每多少小时执行一次
	 * @return obj
	 */
	public static Object getEveryHourSchedule(int hours) {
		return SimpleScheduleBuilder.repeatHourlyForever(hours);
	}

	/**
	 * 获取一个按分钟的任务周期，可直接使用
	 * 
	 * @param minutes
	 *            每多少分钟执行一次
	 * @return obj
	 */
	public static Object getEveryMinuteSchedule(int minutes) {
		return SimpleScheduleBuilder.repeatMinutelyForever(minutes);
	}

	/**
	 * 获取一个按秒的任务周期，可直接使用
	 * 
	 * @param seconds
	 *            每多少秒执行一次
	 * @return obj
	 */
	public static Object getEverySecondSchedule(int seconds) {
		return SimpleScheduleBuilder.repeatSecondlyForever(seconds);
	}

	/**
	 * 设置秒
	 * 
	 * @param second
	 *            秒
	 * @return TaskScheduleBuilder
	 */
	public TaskScheduleBuilder setSecond(String second) {
		this.second = second;
		return this;
	}

	/**
	 * 设置分钟
	 * 
	 * @param minute
	 *            分钟
	 * @return TaskScheduleBuilder
	 */
	public TaskScheduleBuilder setMinute(String minute) {
		this.minute = minute;
		return this;
	}

	/**
	 * 设置小时
	 * 
	 * @param hour
	 *            小时
	 * @return TaskScheduleBuilder
	 */
	public TaskScheduleBuilder setHour(String hour) {
		this.hour = hour;
		return this;
	}

	/**
	 * 设置日期
	 * 
	 * @param day
	 *            日期
	 * @return TaskScheduleBuilder
	 */
	public TaskScheduleBuilder setDay(String day) {
		this.day = day;
		this.week = "?";
		return this;
	}

	/**
	 * 设置月份
	 * 
	 * @param month
	 *            月份
	 * @return TaskScheduleBuilder
	 */
	public TaskScheduleBuilder setMonth(String month) {
		this.month = month;
		return this;
	}

	/**
	 * 设置星期
	 * 
	 * @param week
	 *            星期
	 * @return TaskScheduleBuilder
	 */
	public TaskScheduleBuilder setWeek(String week) {
		this.week = week;
		this.day = "?";
		return this;
	}

	/**
	 * 设置年
	 * 
	 * @param year
	 *            年
	 * @return TaskScheduleBuilder
	 */
	public TaskScheduleBuilder setYear(String year) {
		this.year = year;
		return this;
	}

	/**
	 * 返回任务周期表达式
	 * 
	 * @return 周期表达式
	 */
	public String getSchedule() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.second).append(" ").append(this.minute).append(" ").append(this.hour)
				.append(" ").append(this.day).append(" ").append(this.month).append(" ")
				.append(this.week).append(" ").append(this.year);
		String str = sb.toString().trim();
		sb.delete(0,sb.length());
		logger.debug("getSchedule:" + str);
		return str;
	}
}
