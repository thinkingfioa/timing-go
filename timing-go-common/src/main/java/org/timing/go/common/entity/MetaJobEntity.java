package org.timing.go.common.entity;

import java.util.Date;

/**
 * 数据库表 TIMING_GO_META_JOB 的实体类
 *
 * @author thinking_fioa 2020/4/11
 */
public class MetaJobEntity {

  private int id;

  private String metaJobKey;

  private String groupName;

  private String metaJobDesc;

  /**
   * HTTP类型
   */
  private String metaJobType;

  /**
   * Web-页面录入; Register-自动注册.
   */
  private String metaJobSource;

  private Date createTime;

  private Date updateTime;
}
