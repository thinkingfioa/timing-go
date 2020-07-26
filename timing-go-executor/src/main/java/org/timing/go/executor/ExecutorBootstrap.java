package org.timing.go.executor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

/**
 * Executor 启动入口
 *
 * @author thinking_fioa 08/12/2019
 */
@SpringBootApplication
@Component("org.timing.go.executor")
public class ExecutorBootstrap implements CommandLineRunner {

  private static final Logger LOGGER = LogManager.getLogger(ExecutorBootstrap.class);

  public static void main(String[] args) {
    SpringApplication executorBootstrap = new SpringApplication(ExecutorBootstrap.class);
    executorBootstrap.run(args);
  }


  @Override
  public void run(String... args) throws Exception {
    LOGGER.info("hello world");
    Thread.sleep(1000000L);
  }
}
