package org.timing.go.executor.samples.http;

import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.timing.go.executor.samples.cfg.ApplicationCfg;
import org.timing.go.executor.samples.cfg.MetaJobHttpCfg;

/**
 * HTTP 类型的执行器案例. TODO 提供注解拦截器.
 *
 * 1. 暴露HTTP访问接口
 *
 * 2. 将MetaJobHttp内容注册到zookeeper中
 *
 * @author thinking_fioa 2020/4/12
 */
@Component
@Order(value = 1)
public class HttpExectorBoostrap implements CommandLineRunner {

  private static final Logger LOGGER = LogManager.getLogger(HttpExectorBoostrap.class);

  @Resource
  private ApplicationCfg appCfg;

  @Resource
  private MetaJobHttpCfg jobHttpCfg;

  @Override
  public void run(String... args) throws Exception {
    LOGGER.info("Http Executor {} start {}:{}, zkRootNampspace {}, zkSubNamespace {}",
        appCfg.getAppName(), appCfg.getServerAddress(), appCfg.getServerPort(),
        jobHttpCfg.getZkRootNamespace(), jobHttpCfg.getZkSubNamespace());
  }
}
