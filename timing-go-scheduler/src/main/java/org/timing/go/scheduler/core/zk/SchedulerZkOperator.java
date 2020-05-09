package org.timing.go.scheduler.core.zk;

import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.timing.go.common.coordinator.ZkCoordinator;
import org.timing.go.common.exception.InvalidArgumentException;
import org.timing.go.scheduler.cfg.SchedulerCfg;
import org.timing.go.scheduler.util.CommonUtils;

/**
 * 调度器的Zk操作
 *
 * @author thinking_fioa 2020/5/9
 */
@Configuration
public class SchedulerZkOperator {

  private static final Logger LOGGER = LogManager.getLogger(SchedulerZkOperator.class);

  @Resource
  private SchedulerCfg zkCfg;

  private ZkCoordinator zkCoordinator;

  @Bean
  ZkCoordinator getZkCoordinator() {
    if (zkCoordinator == null) {
      if (CommonUtils.emptyCheck(zkCfg.getZkQuorum())) {
        LOGGER.warn("zk quorum empty.");
        throw new InvalidArgumentException("zk quorum empty");
      }

      LOGGER.info("try to init zkOperator. {}", zkCfg.getZkQuorum());

      zkCoordinator = new ZkCoordinator(zkCfg);
      // 初始化
      zkCoordinator.init();
    }

    return zkCoordinator;
  }

}
