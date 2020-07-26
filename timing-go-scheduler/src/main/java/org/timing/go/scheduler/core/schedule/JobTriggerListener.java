package org.timing.go.scheduler.core.schedule;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Trigger;
import org.quartz.listeners.TriggerListenerSupport;

/**
 * 作业触发监听器.
 *
 * @author thinking_fioa 2020/2/16
 */
public class JobTriggerListener extends TriggerListenerSupport {

  private static final Logger LOGGER = LogManager.getLogger(JobTriggerListener.class);

  private static final String NAME = "JobTriggerListener";

  /**
   * 定义返回监听器的名字
   */
  @Override
  public String getName() {
    return NAME;
  }

  /**
   * Job触发Misfired
   */
  @Override
  public void triggerMisfired(final Trigger trigger) {
    LOGGER.warn("jobKey {} triggerKey {} trigger misfired. lastFireTime {}", trigger.getJobKey(),
        trigger.getKey(), trigger.getPreviousFireTime());
  }
}
