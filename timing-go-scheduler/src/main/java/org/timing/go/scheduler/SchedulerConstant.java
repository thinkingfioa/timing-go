package org.timing.go.scheduler;

/**
 * 项目Scheduler 常量.
 *
 * @author thinking_fioa 2020/2/9
 */
public final class SchedulerConstant {

  private SchedulerConstant() {
    throw new UnsupportedOperationException("static class");
  }

  /**
   * 元Job来源方式。自动注册.
   */
  public static final String META_JOB_SOURCE_REGISTER = "Register";


  /**
   * 元Job来源方式。页面添加.
   */
  public static final String META_JOB_SOURCE_WEB = "Web";
}
