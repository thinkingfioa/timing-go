package org.timing.go.common.coordinator;

import java.nio.charset.Charset;

/**
 * Zookeeper 配置抽象类.
 *
 * @author thinking_fioa 2020/2/2
 */
public interface IZkCfg {

  /**
   * Zookeeper 集群地址. 格式为${ip1}:2181,${ip2}:2181
   */
  String getZkQuorum();

  int getZkRetryBaseSleepTime();

  int getZkMaxRetries();

  int getZkRetryMaxSleepTime();

  int getSessionTimemoutMs();

  int getConnectionTimeoutMs();

  String getZkRootNamespace();

  String getProjectZkSubNamespace();

  Charset dataCharset();
}
