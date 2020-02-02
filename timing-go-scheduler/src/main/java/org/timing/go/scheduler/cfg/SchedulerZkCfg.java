package org.timing.go.scheduler.cfg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.timing.go.common.coordinator.IZkCfg;

/**
 * 调度器 Zookeeper 配置. scheduler_zk.properties
 *
 * @author thinking_fioa 2020/1/5
 */

@Component
@PropertySource(value = {"classpath:scheduler_zk.properties"})
public class SchedulerZkCfg implements IZkCfg {

  /**
   * 格式为${ip1}:2181,${ip2}:2181
   */
  @Value(value = "${zookeeper.quorum}")
  private String zkQuorum;

  /**
   * unit ms
   */
  @Value(value = "${zookeeper.base.sleep.time")
  private int zkRetryBaseSleepTime;

  @Value(value = "${zookeeper.max.retries")
  private int zkMaxRetries;

  /**
   * unit ms
   */
  @Value(value = "${zookeeper.retry.max.sleep.time}")
  private int zkRetryMaxSleepTime;

  @Value(value = "${zookeeper.namespace}")
  private String namespace;

  @Value(value = "${zookeeper.session.timeout.ms}")
  private int sessionTimemoutMs;

  @Value(value = "${zookeeper.connection.timeout.ms}")
  private int connectionTimeoutMs;

  @Override
  public String getZkQuorum() {
    return zkQuorum;
  }

  @Override
  public int getZkRetryBaseSleepTime() {
    return zkRetryBaseSleepTime;
  }

  @Override
  public int getZkMaxRetries() {
    return zkMaxRetries;
  }

  @Override
  public int getZkRetryMaxSleepTime() {
    return zkRetryMaxSleepTime;
  }

  @Override
  public String getNamespace() {
    return namespace;
  }

  @Override
  public int getSessionTimemoutMs() {
    return sessionTimemoutMs;
  }

  @Override
  public int getConnectionTimeoutMs() {
    return connectionTimeoutMs;
  }
}
