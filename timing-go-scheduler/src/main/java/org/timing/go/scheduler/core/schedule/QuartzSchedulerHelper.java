package org.timing.go.scheduler.core.schedule;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.timing.go.common.util.CommonUtils;

/**
 * Quartz 调度器帮助器.
 *
 * @author thinking_fioa 2020/2/16
 */
public class QuartzSchedulerHelper {

  private static final Logger LOGGER = LogManager.getLogger(QuartzSchedulerHelper.class);

  private QuartzSchedulerHelper() {
    throw new UnsupportedOperationException("static class.");
  }


  /**
   * 构建CronSchedule.
   */
  static CronScheduleBuilder buildScheduleBuilder(String cronExpression)
      throws SchedulerException {
    if (legalCronExpress(cronExpression)) {
      LOGGER.warn("cronExrpession is illegal. {}", cronExpression);
      throw new SchedulerException("cronExpression illegal");
    }

    return CronScheduleBuilder.cronSchedule(cronExpression)
        .withMisfireHandlingInstructionDoNothing();
  }

  /**
   * 构建JobDetail.
   */
  static JobDetail buildJobDetail(Class<? extends Job> clazz, JobKey jobKey,
      Map<String, Integer> jobDataMap) {
    JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobKey).build();
    jobDetail.getJobDataMap().putAll(jobDataMap);
    return jobDetail;
  }

  /**
   * 添加属性到map中.
   */
  public static Map<String, Integer> buildDataMap(int jobNameId, int jobGroupNameId) {
    Map<String, Integer> dataMap = new HashMap<>();
    dataMap.put(QuartzConstant.JOB_ID_KEY, jobNameId);
    dataMap.put(QuartzConstant.JOB_NAME_ID_KEY, jobGroupNameId);
    return dataMap;
  }

  public static boolean legalCronExpress(String cronExpress) {
    return !CommonUtils.emptyCheck(cronExpress) && CronExpression.isValidExpression(cronExpress);
  }
}
