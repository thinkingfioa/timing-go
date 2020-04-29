package org.timing.go.rpc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author thinking-ppp 2020/1/5
 */
public class MyJob implements Job {

  private static final Logger LOGGER = LogManager.getLogger(MyJob.class);

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    LOGGER.info("luweilin");
  }
}
