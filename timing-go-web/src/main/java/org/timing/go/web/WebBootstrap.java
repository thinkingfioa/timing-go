package org.timing.go.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author thinking_fioa 2020/2/9
 */
@SpringBootApplication
@ComponentScan({"org.timing.go.web.controller", "org.timing.go.web.service"})
public class WebBootstrap {

  public static void main(String[] args) {
    SpringApplication.run(WebBootstrap.class, args);
  }
}
