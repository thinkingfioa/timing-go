package org.timing.go.common.entity;

import java.util.Date;

/**
 * 数据库表 TIMING_GO_USER 的实体类
 *
 * @author thinking_fioa 2020/4/11
 */
public class UserEntity {

  private int id;

  private String userName;

  private String userPassword;

  /**
   * 0-管理员; 1-普通用户
   */
  private int userType;

  private String email;

  private int phone;

  private Date createTime;

  private Date updateTime;

  public int getId() {
    return id;
  }

  public String getUserName() {
    return userName;
  }

  public String getUserPassword() {
    return userPassword;
  }

  public int getUserType() {
    return userType;
  }

  public String getEmail() {
    return email;
  }

  public int getPhone() {
    return phone;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }
}
