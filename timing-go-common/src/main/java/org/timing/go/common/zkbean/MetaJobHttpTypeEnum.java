package org.timing.go.common.zkbean;

import com.google.gson.annotations.SerializedName;

/**
 * 元Job{@link MetaJobTypeEnum#HTTP}类型请求类型.
 *
 * @author thinking_fioa 2020/5/10
 */
public enum MetaJobHttpTypeEnum {

  /**
   * GET请求.
   */
  @SerializedName("1")
  GET(1, "Get"),

  /**
   * POST请求.
   */
  @SerializedName("2")
  POST(2, "Post");

  private int type;
  private String desc;

  MetaJobHttpTypeEnum(int type, String desc) {
    this.type = type;
    this.desc = desc;
  }

  public int getType() {
    return type;
  }

  public String getDesc() {
    return desc;
  }
}
