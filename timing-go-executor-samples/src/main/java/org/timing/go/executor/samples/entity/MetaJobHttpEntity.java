package org.timing.go.executor.samples.entity;

import com.google.gson.annotations.SerializedName;
import java.util.Collections;
import java.util.List;

/**
 * 保存实体到ZK中.
 *
 * @author thinking_fioa 2020/4/26
 */
public class MetaJobHttpEntity {

  /**
   * 元Job所在的组.
   */
  private String groupName;

  /**
   * 元Job的应用名称.
   */
  private String appName;

  /**
   * IP地址
   */
  private String ip;

  /**
   * 端口
   */
  private int port;

  /**
   * 请求的Http路径
   */
  private String httpPath;

  /**
   * 请求类型. GET或POST请求
   */
  private HttpTypeEnum httpType;

  /**
   * 请求类名
   */
  private String clazzName;

  /**
   * 请求方法名.
   */
  private String methodName;

  /**
   * 输出类名.
   */
  private String outputClazz;

  /**
   * 输出参数.
   */
  private List<String> inputList;

  /**
   * 元Job的描述.
   */
  private String metaJobHttpDesc;

  public MetaJobHttpEntity(String groupName, String appName, String ip, int port, String httpPath,
      HttpTypeEnum httpType, Class<?> clazz, String methodName, Class<?> outputClazz,
      List<String> inputList, String metaJobHttpDesc) {
    this.groupName = groupName;
    this.appName = appName;
    this.ip = ip;
    this.port = port;
    this.httpPath = httpPath;
    this.httpType = httpType;
    clazzName = clazz.getName();
    this.methodName = methodName;
    this.outputClazz = outputClazz.getName();
    this.inputList = Collections.unmodifiableList(inputList);
    this.metaJobHttpDesc = metaJobHttpDesc;
  }

  public String getGroupName() {
    return groupName;
  }

  public String getAppName() {
    return appName;
  }

  public String getIp() {
    return ip;
  }

  public int getPort() {
    return port;
  }

  public String getHttpPath() {
    return httpPath;
  }

  public HttpTypeEnum getHttpType() {
    return httpType;
  }

  public String getClazzName() {
    return clazzName;
  }

  public String getMethodName() {
    return methodName;
  }

  public String getOutputClazz() {
    return outputClazz;
  }

  public List<String> getInputList() {
    return inputList;
  }

  public String getMetaJobHttpDesc() {
    return metaJobHttpDesc;
  }

  public enum HttpTypeEnum {
    /**
     * HTTP 请求类型 GET.
     */
    @SerializedName("1")
    GET("1", "GET"),

    /**
     * HTTP 请求类型 POST.
     */
    @SerializedName("2")
    POST("2", "POST");

    private final String httpType;
    private final String httpTypeDesc;

    HttpTypeEnum(String httpType, String httpTypeDesc) {
      this.httpType = httpType;
      this.httpTypeDesc = httpTypeDesc;
    }

    public String getHttpType() {
      return httpType;
    }

    public String getHttpTypeDesc() {
      return httpTypeDesc;
    }
  }
}
