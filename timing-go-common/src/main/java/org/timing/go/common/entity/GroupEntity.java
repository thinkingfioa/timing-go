package org.timing.go.common.entity;

import java.util.Date;

/**
 * 数据库表 TIMING_GO_GROUP 的实体类
 *
 * @author thinking_fioa 2020/4/11
 */
public class GroupEntity {

  private int id;

  private int projectId;

  private String groupName;

  private String groupDesc;

  /**
   * 0-未删除, 1-已删除
   */
  private int deleted;

  private Date createTime;

  private Date updateTime;

  public int getId() {
    return id;
  }

  public int getProjectId() {
    return projectId;
  }

  public String getGroupName() {
    return groupName;
  }

  public String getGroupDesc() {
    return groupDesc;
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
