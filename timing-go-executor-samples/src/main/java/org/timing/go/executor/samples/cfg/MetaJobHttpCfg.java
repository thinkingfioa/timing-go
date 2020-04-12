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

  @Value(value = "${http.zookeeper.self.namespace:MetaJobHttp}")
  private String zkSelfNamespace;

  public String getZkSelfNamespace() {
    return zkSelfNamespace;
  }
}
