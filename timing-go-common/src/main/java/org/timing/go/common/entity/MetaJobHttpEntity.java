package org.timing.go.common.entity;

import java.util.Date;

/**
 * 数据库表 TIMING_GO_META_JOB_HTTP 的实体类
 *
 * @author thinking_fioa 2020/4/11
 */
public class MetaJobHttpEntity {

  private int id;

  private String metaJobKey;

  private String httpAppName;

  private String httpPath;

  private String httpIp;

  private int httpPort;

  /**
   * 0-GET; 1-POST
   */
  private int httpType;

  private int httpParamFlag;

  private int httpParamContent;

  private Date createTime;

  private Date updateTime;

  public int getId() {
    return id;
  }

  public String getMetaJobKey() {
    return metaJobKey;
  }

  public String getHttpAppName() {
    return httpAppName;
  }

  public String getHttpPath() {
    return httpPath;
  }

  public String getHttpIp() {
    return httpIp;
  }

  public int getHttpPort() {
    return httpPort;
  }

  public int getHttpType() {
    return httpType;
  }

  public int getHttpParamFlag() {
    return httpParamFlag;
  }

  public int getHttpParamContent() {
    return httpParamContent;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }
}
