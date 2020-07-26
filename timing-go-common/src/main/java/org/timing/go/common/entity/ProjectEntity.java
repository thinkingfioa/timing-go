package org.timing.go.common.entity;

import java.util.Date;

/**
 * 数据库表 TIMING_GO_PROJECT 的实体类
 *
 * @author thinking_fioa 2020/4/11
 */
public class ProjectEntity {

  private int id;

  private String projectName;

  private String projectDesc;

  private int userId;

  /**
   * 0-未删除; 1-已删除
   */
  private int deleted;

  private Date createTime;

  private Date updateTime;

  public int getId() {
    return id;
  }

  public String getProjectName() {
    return projectName;
  }

  public String getProjectDesc() {
    return projectDesc;
  }

  public int getUserId() {
    return userId;
  }

  public int getDeleted() {
    return deleted;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }
}
