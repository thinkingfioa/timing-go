package org.timing.go.scheduler.cfg;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.timing.go.common.coordinator.IZkCfg;

/**
 * 调度器 Zookeeper 配置. scheduler.properties
 *
 * @author thinking_fioa 2020/1/5
 */
@Component
@PropertySource(value = {"classpath:scheduler.properties"})
public class SchedulerCfg implements IZkCfg {

  /**
   * 格式为${ip1}:2181,${ip2}:2181
   */
  @Value(value = "${zk.quorum:127.0.0.1}")
  private String zkQuorum;

  /**
   * unit ms
   */
  @Value(value = "${zk.base.sleep.time:1000}")
  private int zkRetryBaseSleepTime;

  @Value(value = "${zk.max.retries:5}")
  private int zkMaxRetries;

  /**
   * unit ms
   */
  @Value(value = "${zk.retry.max.sleep.time:3000}")
  private int zkRetryMaxSleepTime;

  @Value(value = "${zk.session.timeout.ms:3000}")
  private int sessionTimemoutMs;

  @Value(value = "${zk.connection.timeout.ms:3000}")
  private int connectionTimeoutMs;

  @Value(value = "${zk.root.namespace:TimingGo}")
  private String zkRootNamespace;

  @Value(value = "${project.zk.sub.namespace:Scheduler}")
  private String projectZkSubNamespace;

  @Value(value = "${meta.job.http.zk.sub.namespace:Scheduler}")
  private String metaJobHttpZkSubNs;

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
  public int getSessionTimemoutMs() {
    return sessionTimemoutMs;
  }

  @Override
  public int getConnectionTimeoutMs() {
    return connectionTimeoutMs;
  }

  @Override
  public String getZkRootNamespace() {
    return zkRootNamespace;
  }

  @Override
  public String getProjectZkSubNamespace() {
    return projectZkSubNamespace;
  }

  public String getMetaJobHttpZkSubNs() {
    return metaJobHttpZkSubNs;
  }

  @Override
  public Charset dataCharset() {
    return StandardCharsets.UTF_8;
  }

}
