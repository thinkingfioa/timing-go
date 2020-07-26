package org.timing.go.scheduler.cfg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 配置文件 application.properties
 *
 * @author thinking_fioa 2020/5/9
 */
@Component
public class ApplicationCfg {

  @Value(value = "${spring.application.name:timing-go-scheduler}")
  private String appName;

  @Value(value = "${server.address:0.0.0.0}")
  private String serverAddress;

  @Value(value = "${server.port:9191}")
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
