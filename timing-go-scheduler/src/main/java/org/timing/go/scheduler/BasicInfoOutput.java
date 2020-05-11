package org.timing.go.scheduler;

import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.timing.go.scheduler.cfg.ApplicationCfg;
import org.timing.go.scheduler.cfg.SchedulerCfg;

/**
 * 项目基本信息输出.
 *
 * @author thinking_fioa 2020/4/6
 */
@Component
@Order(value = 1)
public class BasicInfoOutput implements CommandLineRunner {

  private static final Logger LOGGER = LogManager.getLogger(BasicInfoOutput.class);

  @Resource
  private ApplicationCfg appCfg;

  @Resource
  private SchedulerCfg schedulerCfg;

  @Override
  public void run(String... args) throws Exception {
    LOGGER.info("Scheduler {} start {}:{}, zkRootNampspace {}, projectZkSubNamespace {}",
        appCfg.getAppName(), appCfg.getServerAddress(), appCfg.getServerPort(),
        schedulerCfg.getZkRootNamespace(), schedulerCfg.getProjectZkSubNamespace());
  }
}
