package org.timing.go.scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 项目基本信息输出.
 *
 * @author thinking_fioa 2020/4/6
 */
@Component
@Order(value = 1)
public class BasicInfoOutput implements CommandLineRunner {

  private static final Logger LOGGER = LogManager.getLogger(BasicInfoOutput.class);

  @Override
  public void run(String... args) throws Exception {
    // TODO 输出配置信息.
    LOGGER.info("output important info");
  }
}
