package org.timing.go.common.entity;

import java.util.Date;
import java.util.List;
import org.timing.go.common.util.CommonUtils;
import org.timing.go.common.util.GsonUtils;
import org.timing.go.common.zkbean.MetaJobHttpZkBean;

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

  /**
   * 0-无参数; 1-有参数。
   */
  private int httpParamFlag;

  private String httpParamContent;

  private Date createTime;

  private Date updateTime;

  public MetaJobHttpEntity(MetaJobHttpZkBean zkBean) {
    metaJobKey = zkBean.getMetaJobKey();
    httpAppName = zkBean.getAppName();
    httpPath = zkBean.getHttpPath();
    httpIp = zkBean.getIp();
    httpPort = zkBean.getPort();
    httpType = zkBean.getHttpType().getType();
    List<String> inputList = zkBean.getInputList();
    if (CommonUtils.emptyCheck(inputList)) {
      httpParamFlag = 0;
      httpParamContent = null;
    } else {
      httpParamFlag = 1;
      httpParamContent = GsonUtils.toString(inputList);
    }
  }

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

  public String getHttpParamContent() {
    return httpParamContent;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }
}
