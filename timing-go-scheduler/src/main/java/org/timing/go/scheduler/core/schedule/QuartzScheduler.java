package org.timing.go.scheduler.core.schedule;

import java.util.Date;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.timing.go.common.util.CommonUtils;

/**
 * 基于Quartz 实现调度器.
 *
 * @author thinking_fioa 2020/2/16
 */
@Component
@Order(value = 2)
public class QuartzScheduler implements CommandLineRunner {

  private static final Logger LOGGER = LogManager.getLogger(QuartzScheduler.class);

  /**
   * quartz 配置文件
   */
  private static final String QUARTZ_FILE_NAME = "quartz.properties";

  private Scheduler scheduler;

  @Override
  public void run(String... args) throws Exception {
    try {
      SchedulerFactory schedulerFactory = new StdSchedulerFactory(QUARTZ_FILE_NAME);
      scheduler = schedulerFactory.getScheduler();
      // 设置Trigger Misfire 监听器.
      scheduler.getListenerManager().addTriggerListener(new JobTriggerListener());
      // 启动
      start();
    } catch (SchedulerException cause) {
      LOGGER.error("create quartz scheduler fail exit.", cause);
      System.exit(1);
    }

  }


  /**
   * 启动Quartz
   */
  public void start() throws SchedulerException {
    if (!scheduler.isStarted()) {
      scheduler.start();
      LOGGER.info("quartz started.");
    }
  }

  /**
   * 关闭Quartz
   */
  public void shutdown() throws SchedulerException {
    if (!scheduler.isShutdown()) {
      scheduler.shutdown();
      LOGGER.info("quartz shutdown and halt all jobs.");
    }
  }

  /**
   * 添加Job.
   */
  public synchronized boolean addJob(String jobName, String jobGroupName, String cronExpression,
      Map<String, Integer> jobDataMap) throws SchedulerException {
    return addJob(jobName, jobGroupName, new Date(), null, cronExpression, jobDataMap);
  }

  /**
   * 添加Job.
   */
  public synchronized boolean addJob(String jobName, String jobGroupName, Date startDate,
      Date endDate, String cronExpression, Map<String, Integer> jobDataMap)
      throws SchedulerException {
    JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
    TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
    if (scheduler.checkExists(jobKey) || scheduler.checkExists(triggerKey)) {
      LOGGER.warn("jobName {}, jobGroupName {} exist.", jobName, jobGroupName);
      return false;
    }

    CronScheduleBuilder scheduleBuilder = QuartzSchedulerHelper
        .buildScheduleBuilder(cronExpression);
    JobDetail jobDetail = QuartzSchedulerHelper
        .buildJobDetail(ScheduleJobAdapter.class, jobKey, jobDataMap);
    CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
        .startAt(startDate).endAt(endDate).withSchedule(scheduleBuilder).forJob(jobDetail).build();

    Date date = scheduler.scheduleJob(jobDetail, cronTrigger);
    LOGGER.debug("addJob {}-{} success, jobDetail {}, cronTrigger {}, date {}", jobName,
        jobGroupName, jobDetail, cronTrigger, date);
    return true;
  }

  /**
   * 删除Job.
   */
  public synchronized boolean removeJob(String jobName, String jobGroupName)
      throws SchedulerException {
    JobKey jobKey = new JobKey(jobName, jobGroupName);
    if (!scheduler.checkExists(jobKey)) {
      LOGGER.warn("jobName {}, jobGroupName {} not found.", jobName, jobGroupName);
      return false;
    }
    LOGGER.debug("jobName {}, jobGroupName {} remove success.", jobName, jobGroupName);
    scheduler.deleteJob(jobKey);
    return true;
  }

  /**
   * 更新Job CronExpression.
   */
  public synchronized boolean updateJobCron(String jobName, String jobGroupName,
      String cronExpression) throws SchedulerException {
    if (!QuartzSchedulerHelper.legalCronExpress(cronExpression)) {
      LOGGER.warn("jobName {} jobGroupName {} cronExpression illegal. {}", jobName, jobGroupName,
          cronExpression);
      return false;
    }

    TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
    if (!scheduler.checkExists(triggerKey)) {
      LOGGER.warn("jobName {} jobGroupName {} not found.", jobName, jobGroupName);
      return false;
    }
    CronTrigger oldTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
    if (CommonUtils.equalAfterTrim(oldTrigger.getCronExpression(), cronExpression)) {
      return true;
    }

    CronScheduleBuilder newBuilder = QuartzSchedulerHelper.buildScheduleBuilder(cronExpression);
    oldTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(newBuilder).build();

    scheduler.rescheduleJob(triggerKey, oldTrigger);
    LOGGER.debug("jobName {}, jobGroupName {} update cron {} success.", jobName, jobGroupName,
        cronExpression);
    return true;
  }

  public synchronized boolean isPaused(String jobName, String jobGroupName)
      throws SchedulerException {
    TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
    if (!scheduler.checkExists(triggerKey)) {
      LOGGER.warn("jobName {} jobGroupName {} not found.", jobName, jobGroupName);
      return false;
    }
    return TriggerState.PAUSED == scheduler.getTriggerState(triggerKey);
  }

  /**
   * 暂停Trigger，即暂停执行与之关联的所有作业。目前Trigger下仅有一个作业.
   *
   * TODO 需要测试暂停后，Quartz是否会补暂停期间遗漏的.
   */
  public synchronized void pauseJob(String jobName, String jobGroupName) throws SchedulerException {
    TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
    if (!scheduler.checkExists(triggerKey)) {
      LOGGER.warn("jobName {} jobGroupName {} not found.", jobName, jobGroupName);
      return;
    }
    scheduler.pauseTrigger(triggerKey);
  }

  /**
   * 恢复Trigger.
   */
  public synchronized void resumeJob(String jobName, String jobGroupName)
      throws SchedulerException {
    TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
    if (!scheduler.checkExists(triggerKey)) {
      LOGGER.warn("jobName {} jobGroupName {} not found.", jobName, jobGroupName);
      return;
    }
    scheduler.resumeTrigger(triggerKey);
  }

  /**
   * 单次触发.
   */
  public synchronized boolean triggerJob(String jobName, String jobGroupName)
      throws SchedulerException {
    JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
    TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
    if (!scheduler.checkExists(jobKey) || !scheduler.checkExists(triggerKey)) {
      LOGGER.warn("jobName {}, jobGroupName {} not found.", jobName, jobGroupName);
      return false;
    }

    scheduler.triggerJob(jobKey);
    LOGGER.debug("jobName {}, jobGroupName {} trigger success.", jobName, jobGroupName);
    return true;
  }
}
