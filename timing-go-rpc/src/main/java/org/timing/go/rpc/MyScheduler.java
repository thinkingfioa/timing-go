package org.timing.go.rpc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class MyScheduler {

  private static final Logger LOGGER = LogManager.getLogger(MyScheduler.class);

  public static void main(String[] args) throws SchedulerException, InterruptedException {
    // 1、创建调度器Scheduler
    SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    Scheduler scheduler = schedulerFactory.getScheduler();

//
//    //睡眠
//    TimeUnit.MINUTES.sleep(1);
//    scheduler.shutdown();
//    LOGGER.info("--------scheduler shutdown ! ------------");

    String cron = "0 10 22 * * ?";
//    String cron = "0/1 * * * * ?";

    for (int i = 0; i <= 1000; i++) {
      addJob(scheduler, cron, "job_" + i, "job_" + i);
    }
    LOGGER.info("6666666666666666");

  }

  private static void addJob(Scheduler scheduler, String cron, String jobName,
      String triggerName) throws SchedulerException {
    // 2、创建JobDetail实例，并与MyJob类绑定(Job执行内容)
    JobDetail jobDetail = JobBuilder.newJob(MyJob.class).withIdentity(jobName, jobName).build();
    // 3、构建Trigger实例,每隔1s执行一次
    Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerName)
        .startNow()//立即生效
        .withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();//一直执行

    //4、执行
    scheduler.scheduleJob(jobDetail, trigger);
    LOGGER.info("--------scheduler start ! ------------");
    scheduler.start();
  }
}

