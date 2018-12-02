package com.moonkin.task;

import java.util.ArrayList;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 任务管理器
 * 
 * @author 张弛
 * 
 */
public class TaskManager {
	protected static Logger logger = LoggerFactory.getLogger(TaskManager.class);

	public static final String FIRE_COUNT = "JOB_FIRE_COUNT";

	public static final String FIRE_COUNT_ERROR = "JOB_FIRE_COUNT_ERROR";

	public static final String CAN_PAUSE = "JOB_CAN_PAUSE";

	public static final String CAN_REMOVE = "JOB_CAN_REMOVE";

	private static Scheduler sched = null;

	static {
		SchedulerFactory sf = new StdSchedulerFactory();
		try {
			sched = sf.getScheduler();
			sched.start();
		} catch (SchedulerException e) {
			logger.error("create Scheduler error", e);
		}
	}

	/**
	 * 停止所有任务
	 */
	public static void stopAllTask() {
		try {
			sched.shutdown();
			logger.debug("AllTask stop");
		} catch (SchedulerException e) {
			logger.error("stopAllTask error", e);
		}
	}

	/**
	 * 新增任务，已存在的任务将不会被添加
	 * 
	 * @param tasks
	 */
	public static void addTask(BaseTask... tasks) {
		for (BaseTask task : tasks) {
			if (task.taskId == null || task.taskId.isEmpty()) {
				logger.error(tasks.getClass() + " taskId IS NULL");
				continue;
			}
			if (task.taskSchedule == null) {
				logger.error(task.getClass() + " taskSchedule IS NULL");
				continue;
			}
			TriggerBuilder<Trigger> builder = TriggerBuilder.newTrigger()
					.withIdentity(task.taskId, task.taskGroup).withDescription(task.taskName)
					.startNow();

			if (task.taskSchedule instanceof String) {
				String taskSchedule = (String) task.taskSchedule;
				if (taskSchedule == null || taskSchedule.isEmpty()) {
					continue;
				}
				builder.withSchedule(CronScheduleBuilder.cronSchedule(taskSchedule));
			} else if (task.taskSchedule instanceof SimpleScheduleBuilder) {
				SimpleScheduleBuilder simpleScheduleBuilder = (SimpleScheduleBuilder) task.taskSchedule;
				if (simpleScheduleBuilder == null) {
					continue;
				}
				builder.withSchedule(simpleScheduleBuilder);
			}

			JobDetail job = JobBuilder.newJob(task.getClass()).withDescription(task.taskName)
					.withIdentity(task.taskId, task.taskGroup).usingJobData(task.dataMap).build();
			Trigger trigger = builder.build();

			try {
				if (!sched.checkExists(job.getKey())) {
					sched.scheduleJob(job, trigger);
				}
			} catch (SchedulerException e) {
				logger.error("addTask error", e);
			}
		}
	}

	/**
	 * 判断一个任务是否存在
	 * 
	 * @param taskId
	 *            任务id
	 * @return
	 */
	public static boolean isExist(String taskId) {
		return isExist(taskId, Scheduler.DEFAULT_GROUP);
	}

	/**
	 * 判断一个任务是否存在
	 * 
	 * @param taskId
	 *            任务id
	 * @param taskGroup
	 *            组id
	 * @return
	 */
	public static boolean isExist(String taskId, String taskGroup) {
		JobKey jobKey = JobKey.jobKey(taskId, taskGroup);
		boolean exist = false;
		try {
			exist = sched.checkExists(jobKey);
		} catch (SchedulerException e) {
			logger.error("check isExist error", e);
		}
		return exist;
	}

	/**
	 * 暂停一个任务
	 * 
	 * @param taskId
	 *            任务唯一id
	 */
	public static void pauseTask(String taskId) {
		pauseTask(taskId, Scheduler.DEFAULT_GROUP);
	}

	/**
	 * 暂停一个任务
	 * 
	 * @param taskId
	 *            任务唯一id
	 * @param taskGroup
	 *            任务组名称
	 */
	public static void pauseTask(String taskId, String taskGroup) {
		try {
			JobDetail jobDetail = sched.getJobDetail(JobKey.jobKey(taskId, taskGroup));
			if (jobDetail != null && jobDetail.getJobDataMap().getBoolean(TaskManager.CAN_PAUSE)) {
				sched.pauseTrigger(TriggerKey.triggerKey(taskId, taskGroup));
			}
		} catch (SchedulerException e) {
			logger.error("stopTask error", e);
		}
	}

	/**
	 * 恢复一个任务
	 * 
	 * @param taskId
	 *            任务唯一id
	 */
	public static void resumeTask(String taskId) {
		resumeTask(taskId, Scheduler.DEFAULT_GROUP);
	}

	/**
	 * 恢复一个任务
	 * 
	 * @param taskId
	 *            任务唯一id
	 * @param taskGroup
	 *            任务组名称
	 */
	public static void resumeTask(String taskId, String taskGroup) {
		try {
			sched.resumeTrigger(TriggerKey.triggerKey(taskId, taskGroup));
		} catch (SchedulerException e) {
			logger.error("stopTask error", e);
		}
	}

	/**
	 * 删除一个任务
	 * 
	 * @param taskId
	 *            任务唯一id
	 */
	public static void removeTask(String taskId) {
		removeTask(taskId, Scheduler.DEFAULT_GROUP);
	}

	/**
	 * 删除一个任务
	 * 
	 * @param taskId
	 *            任务唯一id
	 * @param taskGroup
	 *            任务组
	 */
	public static void removeTask(String taskId, String taskGroup) {
		try {
			JobDetail jobDetail = sched.getJobDetail(JobKey.jobKey(taskId, taskGroup));
			if (jobDetail != null && jobDetail.getJobDataMap().getBoolean(TaskManager.CAN_REMOVE)) {
				pauseTask(taskId, taskGroup);
				sched.unscheduleJob(TriggerKey.triggerKey(taskId, taskGroup));
			}
		} catch (SchedulerException e) {
			logger.error("removeTask error", e);
		}
	}

	/**
	 * 获得所有任务
	 * 
	 * @return
	 */
	public static List<Trigger> getAllTask() {
		List<Trigger> allTriggers = new ArrayList<Trigger>();
		try {
			List<String> groups = sched.getJobGroupNames();
			for (String group : groups) {
				for (JobKey jobKey : sched.getJobKeys(GroupMatcher.jobGroupEquals(group))) {
					List<? extends Trigger> triggers = sched.getTriggersOfJob(jobKey);
					for (Trigger trigger : triggers) {
						allTriggers.add(trigger);
					}
				}
			}
		} catch (Exception e) {
			logger.error("getAllTask error", e);
		}
		return allTriggers;
	}

	/**
	 * 获得一个job
	 * 
	 * @param jobKey
	 * @return
	 */
	public static JobDetail getJobDetail(JobKey jobKey) {
		JobDetail jobDetail = null;
		try {
			jobDetail = sched.getJobDetail(jobKey);
		} catch (SchedulerException e) {
			logger.error("getJobDetail error", e);
		}
		return jobDetail;
	}

	/**
	 * 获得一个任务的状态
	 * 
	 * @param triggerKey
	 * @return
	 */
	public static TriggerState getJobState(TriggerKey triggerKey) {
		TriggerState state = null;
		try {
			state = sched.getTriggerState(triggerKey);
		} catch (SchedulerException e) {
			logger.error("getJobState error", e);
		}
		return state;
	}
}
