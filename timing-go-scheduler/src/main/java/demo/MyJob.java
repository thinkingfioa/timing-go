package demo;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author thinking-ppp 2020/1/5
 */
public class MyJob implements Job {

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    System.out.println("luweilin");
  }
}
