package org.timing.go.scheduler.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author thinking-ppp 2020/1/19
 *
 * Quartz任务统一接口，"调度模块"与"任务模块"相互解耦，调度模块中统一使用 TimingGoJobBean
 */
public class TimingGoJobBean implements Job {

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    //TODO
  }
}
