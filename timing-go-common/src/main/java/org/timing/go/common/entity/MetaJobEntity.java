package org.timing.go.common.entity;

import java.util.Date;
import org.timing.go.common.zkbean.MetaJobHttpZkBean;

/**
 * 数据库表 TIMING_GO_META_JOB 的实体类
 *
 * @author thinking_fioa 2020/4/11
 */
public class MetaJobEntity {

  private int id;


  /**
   * 元Job的Key
   */
  private String metaJobKey;

  /**
   * 元Job所在的组.
   */
  private String groupName;

  /**
   * 元Job的描述.
   */
  private String metaJobDesc;

  /**
   * HTTP类型
   */
  private String metaJobType;

  /**
   * Web-页面录入; Register-自动注册.
   */
  private String metaJobSource;

  /**
   * 创建时间.
   */
  private Date createTime;

  /**
   * 更新时间.
   */
  private Date updateTime;

  public MetaJobEntity(MetaJobHttpZkBean zkBean) {
    metaJobKey = zkBean.getMetaJobKey();
    groupName = zkBean.getGroupName();
    metaJobDesc = zkBean.getMetaJobDesc();
    metaJobType = zkBean.getMetaJobType().getType();
    metaJobSource = zkBean.getMetaJobSource().getSource();
  }

  public int getId() {
    return id;
  }

  public String getMetaJobKey() {
    return metaJobKey;
  }

  public String getGroupName() {
    return groupName;
  }

  public String getMetaJobDesc() {
    return metaJobDesc;
  }

  public String getMetaJobType() {
    return metaJobType;
  }

  public String getMetaJobSource() {
    return metaJobSource;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }
}
