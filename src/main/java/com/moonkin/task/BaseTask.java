package com.moonkin.task;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务基类
 * 
 * @author 张弛
 * 
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public abstract class BaseTask implements Job {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 任务唯一id
	 */
	String taskId;

	/**
	 * 任务名称
	 */
	String taskName;

	/**
	 * 任务组，默认为DEFAULT
	 */
	String taskGroup = Scheduler.DEFAULT_GROUP;

	/**
	 * 任务执行周期
	 */
	Object taskSchedule;

	/**
	 * 任务所带参数
	 */
	JobDataMap dataMap = new JobDataMap();

	public BaseTask() {
		super();
		setCanPause(false);
		setCanRemove(false);
		config();
		dataMap.put(TaskManager.FIRE_COUNT, 0);
		dataMap.put(TaskManager.FIRE_COUNT_ERROR, 0);
	}

	/**
	 * 设置任务的详细信息
	 */
	protected abstract void config();

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		int fireCount = jobDataMap.getInt(TaskManager.FIRE_COUNT);
		int errorFireCount = jobDataMap.getInt(TaskManager.FIRE_COUNT_ERROR);
		try {
			logger.debug("[GROUP:" + taskGroup + "]-[TASK:" + taskName + "] START.");
			doWork(jobDataMap);
			logger.debug("[GROUP:" + taskGroup + "]-[TASK:" + taskName + "] FINISH.");
		} catch (Exception e) {
			logger.error("[GROUP:" + taskGroup + "]-[TASK:" + taskName + "] FAIL", e);
			jobDataMap.put(TaskManager.FIRE_COUNT_ERROR, ++errorFireCount);
		} finally {
			jobDataMap.put(TaskManager.FIRE_COUNT, ++fireCount);
		}
	}

	/**
	 * 任务业务执行
	 */
	protected abstract void doWork(JobDataMap dataMap);

	public void addParam(String key, Object value) {
		dataMap.put(key, value);
	}

	public String getTaskId() {
		return taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public String getTaskGroup() {
		return taskGroup;
	}

	public Object getTaskSchedule() {
		return taskSchedule;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public void setTaskGroup(String taskGroup) {
		this.taskGroup = taskGroup;
	}

	public void setTaskSchedule(Object taskSchedule) {
		this.taskSchedule = taskSchedule;
	}

	/**
	 * 设置是否可以暂停
	 * 
	 * @param can
	 */
	public void setCanPause(boolean can) {
		dataMap.put(TaskManager.CAN_PAUSE, can);
	}

	/**
	 * 设置是否可以删除
	 * 
	 * @param can
	 */
	public void setCanRemove(boolean can) {
		dataMap.put(TaskManager.CAN_REMOVE, can);
	}
}
