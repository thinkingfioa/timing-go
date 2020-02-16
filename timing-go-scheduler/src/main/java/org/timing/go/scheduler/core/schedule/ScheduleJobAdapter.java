package org.timing.go.scheduler.core.schedule;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 调度作业适配器.
 *
 * @author thinking_fioa 2020/2/16
 */
public class ScheduleJobAdapter implements Job {

  private static final Logger LOGGER = LogManager.getLogger(ScheduleJobAdapter.class);

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
    int jobId = jobDataMap.getInt(QuartzConstant.JOB_ID_KEY);
    int jobGroupId = jobDataMap.getInt(QuartzConstant.JOB_NAME_ID_KEY);
    LOGGER.info("jobId {} jobGroupId {} ready to exector.", jobId, jobGroupId);
  }
}
