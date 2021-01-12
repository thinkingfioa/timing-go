package org.timing.go.interceptor;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 拦截Http加载的Bean，实现自动注册到zookeeper中
 *
 * @author thinking_fioa 2021/1/12
 */
public class HttpInterceptorListener implements ApplicationListener<ApplicationEvent> {

  @Override
  public void onApplicationEvent(ApplicationEvent event) {
    if (event instanceof ContextRefreshedEvent) {
      // TODO 补充注册到zookeeper中逻辑
    }
  }
}
