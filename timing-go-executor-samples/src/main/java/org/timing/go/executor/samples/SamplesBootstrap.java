package org.timing.go.executor.samples;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

/**
 * Samples 启动类.
 *
 * @author thinking_fioa 2020/4/12
 */
@SpringBootApplication
@Component("org.timing.executor.samples")
public class SamplesBootstrap {

  private static final Logger LOGGER = LogManager.getLogger(SamplesBootstrap.class);

  public static void main(String[] args) {
    SpringApplication.run(SamplesBootstrap.class, args);
    LOGGER.info("Executor Samples starting.");
  }
}
