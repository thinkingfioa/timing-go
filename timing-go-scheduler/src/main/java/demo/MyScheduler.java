package demo;

import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class MyScheduler {

  private static final Logger LOGGER = LogManager.getLogger(MyScheduler.class);

  public static void main(String[] args) throws SchedulerException, InterruptedException {
    // 1、创建调度器Scheduler
    SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    Scheduler scheduler = schedulerFactory.getScheduler();
//    // 2、创建JobDetail实例，并与MyJob类绑定(Job执行内容)
//    JobDetail jobDetail = JobBuilder.newJob(MyJob.class).withIdentity("job1", "group1").build();
//    // 3、构建Trigger实例,每隔1s执行一次
//    Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "triggerGroup1")
//        .startNow()//立即生效
//        .withSchedule(CronScheduleBuilder.cronSchedule("*/5 * * * * ?")).build();//一直执行
//
//    //4、执行
//    scheduler.scheduleJob(jobDetail, trigger);
    LOGGER.info("--------scheduler start ! ------------");
    scheduler.start();

    //睡眠
    TimeUnit.MINUTES.sleep(1);
    scheduler.shutdown();
    LOGGER.info("--------scheduler shutdown ! ------------");


  }
}

