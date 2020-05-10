package org.timing.go.common.zkbean;

import com.google.gson.annotations.SerializedName;

/**
 * 元Job来源.
 *
 * @author thinking_fioa 2020/5/10
 */
public enum MetaJobSourceEnum {

  /**
   * 来自于Web端注册.
   */
  @SerializedName("Web")
  WEB("Web"),

  /**
   * 来自于自动组册.
   */
  @SerializedName("Register")
  REGISTER("Register");

  private String source;

  MetaJobSourceEnum(String source) {
    this.source = source;
  }

  public String getSource() {
    return source;
  }
}
