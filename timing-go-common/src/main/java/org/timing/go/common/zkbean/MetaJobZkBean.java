package org.timing.go.common.zkbean;

/**
 * @author thinking_fioa 2020/5/10
 */
public class MetaJobZkBean {

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
   * 元Job的类型. HTTP
   */
  private MetaJobTypeEnum metaJobType;

  /**
   * 元Job来源
   */
  private MetaJobSourceEnum metaJobSource;

  public MetaJobZkBean(String metaJobKey, String groupName, String metaJobDesc,
      MetaJobTypeEnum metaJobType, MetaJobSourceEnum metaJobSource) {
    this.metaJobKey = metaJobKey;
    this.groupName = groupName;
    this.metaJobDesc = metaJobDesc;
    this.metaJobType = metaJobType;
    this.metaJobSource = metaJobSource;
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

  public MetaJobTypeEnum getMetaJobType() {
    return metaJobType;
  }

  public MetaJobSourceEnum getMetaJobSource() {
    return metaJobSource;
  }
}
