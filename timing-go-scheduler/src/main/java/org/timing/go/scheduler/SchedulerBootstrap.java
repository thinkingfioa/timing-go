package org.timing.go.scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

/**
 * Scheduler 启动入口
 *
 * 依次启动顺序
 * <pre>
 *   1. {@link BasicInfoOutput}
 *   2. {@link org.timing.go.scheduler.core.schedule.QuartzScheduler}
 *   3. {@link org.timing.go.scheduler.core.zk.MetaJobHttpZkListener}
 * </pre>
 *
 * @author thinking_fioa 08/12/2019
 */
@SpringBootApplication
@Component("org.timing.go.scheduler")
public class SchedulerBootstrap {

  private static final Logger LOGGER = LogManager.getLogger(SchedulerBootstrap.class);

  public static void main(String[] args) {
    SpringApplication.run(SchedulerBootstrap.class, args);
    LOGGER.info("Scheduler starting.");
  }
}
