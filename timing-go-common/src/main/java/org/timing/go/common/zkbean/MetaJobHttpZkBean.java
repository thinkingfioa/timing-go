package org.timing.go.common.zkbean;

import java.util.List;

/**
 * 元Job与Zookeeper交互的Bean.
 *
 * @author thinking_fioa 2020/5/10
 */
public class MetaJobHttpZkBean extends MetaJobZkBean {

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
  private MetaJobHttpTypeEnum httpType;

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
   * TODO 提供Builder.
   */
  public MetaJobHttpZkBean(String metaJobKey, String groupName, String metaJobDesc,
      MetaJobTypeEnum metaJobType, MetaJobSourceEnum metaJobSource, String appName,
      String ip, int port, String httpPath, MetaJobHttpTypeEnum httpType, Class<?> clazzName,
      String methodName, Class<?> outputClazz, List<String> inputList) {
    super(metaJobKey, groupName, metaJobDesc, metaJobType, metaJobSource);
    this.appName = appName;
    this.ip = ip;
    this.port = port;
    this.httpPath = httpPath;
    this.httpType = httpType;
    this.clazzName = clazzName.getName();
    this.methodName = methodName;
    this.outputClazz = outputClazz.getName();
    this.inputList = inputList;
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

  public MetaJobHttpTypeEnum getHttpType() {
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

}
