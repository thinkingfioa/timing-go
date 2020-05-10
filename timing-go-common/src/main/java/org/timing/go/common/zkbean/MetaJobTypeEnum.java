package org.timing.go.common.zkbean;

import com.google.gson.annotations.SerializedName;

/**
 * 元Job的类型.
 *
 * @author thinking_fioa 2020/5/10
 */
public enum MetaJobTypeEnum {

  /**
   * HTTP类型.
   */
  @SerializedName("Http")
  HTTP("Http");

  private String type;

  MetaJobTypeEnum(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
