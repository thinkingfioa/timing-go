package org.timing.go.executor.samples.cfg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author thinking_fioa 2020/4/12
 */
@Component
public class ApplicationCfg {

  @Value(value = "${spring.application.name:http-executor-samples}")
  private String appName;

  @Value(value = "${server.address:0.0.0.0}")
  private String serverAddress;

  @Value(value = "${server.port:8080}")
  private int serverPort;

  public String getAppName() {
    return appName;
  }

  public String getServerAddress() {
    return serverAddress;
  }

  public int getServerPort() {
    return serverPort;
  }
}
