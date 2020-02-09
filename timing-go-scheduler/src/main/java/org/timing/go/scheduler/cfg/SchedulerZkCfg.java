package org.timing.go.scheduler.cfg;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
  @Value(value = "${zookeeper.quorum:127.0.0.1}")
  private String zkQuorum;

  /**
   * unit ms
   */
  @Value(value = "${zookeeper.base.sleep.time:1000}")
  private int zkRetryBaseSleepTime;

  @Value(value = "${zookeeper.max.retries:5}")
  private int zkMaxRetries;

  /**
   * unit ms
   */
  @Value(value = "${zookeeper.retry.max.sleep.time:3000}")
  private int zkRetryMaxSleepTime;

  @Value(value = "${zookeeper.namespace:/timing-go/scheduler}")
  private String namespace;

  @Value(value = "${zookeeper.session.timeout.ms:3000}")
  private int sessionTimemoutMs;

  @Value(value = "${zookeeper.connection.timeout.ms:3000}")
  private int connectionTimeoutMs;

  @Value(value = "${zookeeper.charsets:UTF-8")
  private Charset dataCharset;

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

  @Override
  public Charset dataCharset() {
    return StandardCharsets.UTF_8;
  }
}
