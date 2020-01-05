package org.timing.go.scheduler.cfg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 调度器 Zookeeper 配置. scheduler_zk.properties
 *
 * @author thinking_fioa 2020/1/5
 */

@Component
@PropertySource(value = {"classpath:scheduler_zk.properties"})
public class SchedulerZkCfg {

  @Value(value = "${zookeeper.quorum}")
  private String zkQuorum;

  public String getZkQuorum() {
    return zkQuorum;
  }
}
