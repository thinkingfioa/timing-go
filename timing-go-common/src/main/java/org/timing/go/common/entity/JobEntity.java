package org.timing.go.common.entity;

import java.util.Date;

/**
 * 数据库表 TIMING_GO_JOB 的实体类
 *
 * @author thinking_fioa 2020/4/11
 */
public class JobEntity {

  private int id;

  private String jobKey;

  private String groupName;

  private String jobTriggerType;

  private String jobTriggerValue;

  private String jobDesc;

  private String jobAlarmEmail;

  private Date createTime;

  private Date updateTime;

  private String jobParentKey;

  private String jobPlan;

  public int getId() {
    return id;
  }

  public String getJobKey() {
    return jobKey;
  }

  public String getGroupName() {
    return groupName;
  }

  public String getJobTriggerType() {
    return jobTriggerType;
  }

  public String getJobTriggerValue() {
    return jobTriggerValue;
  }

  public String getJobDesc() {
    return jobDesc;
  }

  public String getJobAlarmEmail() {
    return jobAlarmEmail;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public String getJobParentKey() {
    return jobParentKey;
  }

  public String getJobPlan() {
    return jobPlan;
  }
}
