package org.timing.go.scheduler;

import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.timing.go.scheduler.cfg.SchedulerZkCfg;
import org.timing.go.scheduler.core.schedule.QuartzScheduler;

/**
 * Scheduler 启动入口
 *
 * @author thinking_fioa 08/12/2019
 */
@SpringBootApplication
@Component("org.timing.go.scheduler")
public class SchedulerBootstrap implements CommandLineRunner {

  private static final Logger LOGGER = LogManager.getLogger(SchedulerBootstrap.class);

  @Resource
  private SchedulerZkCfg zkCfg;

  public static void main(String[] args) {
    SpringApplication bootstrap = new SpringApplication(SchedulerBootstrap.class);
    bootstrap.run(args);
  }

  @Override
  public void run(String... args) throws Exception {
    try {
      QuartzScheduler.getInstance().start();
      LOGGER.info("scheduler bootstrap start success.");
    } catch (Throwable cause) {
      LOGGER.error("scheduler bootstrap fail.", cause);
    }
  }
}
