package org.timing.go.executor.samples.cfg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * MetaJobHttp 配置. meta_job_http.properties
 *
 * @author thinking_fioa 2020/4/12
 */
@Component
@PropertySource(value = {"classpath:meta_job_http.properties"})
public class MetaJobHttpCfg {

  @Value(value = "${zk.quorum:localhost:2181}")
  private String zkQuorum;

  @Value(value = "${zk.base.sleep.time:1000}")
  private int zkBaseSleepTime;

  @Value(value = "${zk.max.retries:5}")
  private int zkMaxRetries;

  @Value(value = "${zk.retry.max.sleep.time:3000}")
  private int zkRetryMaxSleepTime;

  @Value(value = "${zk.session.timeout.ms:3000}")
  private int zkSessionTimeoutMs;

  @Value(value = "${zk.connection.timeout.ms:3000}")
  private int zkConnectionTimeoutMs;

  @Value(value = "${zk.root.namespace:/TimingGo}")
  private String zkRootNamespace;

  @Value(value = "${zk.sub.namespace:MetaJobHttp}")
  private String zkSubNamespace;

  @Value(value = "${http.group.name:TestHttpGroup}")
  private String groupName;

  public String getZkQuorum() {
    return zkQuorum;
  }

  public int getZkBaseSleepTime() {
    return zkBaseSleepTime;
  }

  public int getZkMaxRetries() {
    return zkMaxRetries;
  }

  public int getZkRetryMaxSleepTime() {
    return zkRetryMaxSleepTime;
  }

  public int getZkSessionTimeoutMs() {
    return zkSessionTimeoutMs;
  }

  public int getZkConnectionTimeoutMs() {
    return zkConnectionTimeoutMs;
  }

  public String getZkRootNamespace() {
    return zkRootNamespace;
  }

  public String getZkSubNamespace() {
    return zkSubNamespace;
  }

  public String getGroupName() {
    return groupName;
  }


}
