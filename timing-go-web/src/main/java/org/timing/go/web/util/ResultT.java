

package org.timing.go.web.util;

/**
 * result
 */
public class ResultT<T> {

  /**
   * status 状态码
   */
  private Integer code;

  /**
   * message 消息
   */
  private String msg;

  /**
   * data
   */
  private T data;

  public ResultT() {
  }

  public ResultT(Integer code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "{code=" + code + ", msg=" + msg + ", data=" + data + "}";
  }
}
