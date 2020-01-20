package org.timing.go.scheduler.schedule;

import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

/**
 * @author thinking-ppp 2020/1/19
 *
 * TimingGo 的调度器
 */
public class TimingGoScheduler {

  private static final Logger LOGGER = LogManager.getLogger(TimingGoScheduler.class);

  private static Scheduler scheduler;

  /**
   * 添加并触发任务
   */
  public static boolean addJob(String jobName, String cronExpression) throws SchedulerException {
    // 1. job key
    TriggerKey triggerKey = TriggerKey.triggerKey(jobName);
    JobKey jobKey = new JobKey(jobName);

    // 2. valid
    if (scheduler.checkExists(triggerKey)) {
      return true;    // PASS
    }

    // 3. corn trigger
    CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression)
        .withMisfireHandlingInstructionDoNothing();   // withMisfireHandlingInstructionDoNothing 忽略掉调度终止过程中忽略的调度
    CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
        .withSchedule(cronScheduleBuilder).build();

    // 4. job detail
    Class<? extends Job> jobClass = TimingGoJobBean.class;
    JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobKey).build();

    // 5. schedule job
    Date date = scheduler.scheduleJob(jobDetail, cronTrigger);

    LOGGER.info(">>>>>>>>>>> addJob success(quartz), jobDetail:{}, cronTrigger:{}, date:{}",
        jobDetail, cronTrigger, date);

    return true;
  }

  /**
   * 移除任务
   */
  public static boolean removeJob(String jobName) throws SchedulerException {

    JobKey jobKey = new JobKey(jobName);
    scheduler.deleteJob(jobKey);

    LOGGER.info(">>>>>>>>>>> removeJob success(quartz), jobKey:{}", jobKey);
    return true;
  }

  /**
   * 更新任务的Cron表达式
   */
  public static boolean updateJobCron(String jobName, String cronExpression)
      throws SchedulerException {

    // 1. job key
    TriggerKey triggerKey = TriggerKey.triggerKey(jobName);

    // 2. valid
    if (!scheduler.checkExists(triggerKey)) {
      return true;    // PASS
    }

    CronTrigger oldTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);

    // 3. avoid repeat cron
    String oldCron = oldTrigger.getCronExpression();
    if (oldCron.equals(cronExpression)) {
      return true;    // PASS
    }

    // 4. new cron trigger
    CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression)
        .withMisfireHandlingInstructionDoNothing();
    oldTrigger = oldTrigger.getTriggerBuilder().withIdentity(triggerKey)
        .withSchedule(cronScheduleBuilder).build();

    // 5. rescheduleJob
    scheduler.rescheduleJob(triggerKey, oldTrigger);

    LOGGER.info(">>>>>>>>>>> resumeJob success, JobName:{}", jobName);
    return true;
  }


}
