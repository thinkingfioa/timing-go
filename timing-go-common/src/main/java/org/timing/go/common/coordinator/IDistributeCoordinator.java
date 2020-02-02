package org.timing.go.common.coordinator;

import java.util.List;
import org.apache.curator.framework.state.ConnectionStateListener;

/**
 * 分布式协调
 *
 * @author thinking_fioa 2020/1/5
 */
public interface IDistributeCoordinator<T> {

  /**
   * 初始化分布式协调器.
   */
  void init();

  /**
   * 启动
   */
  void start();

  /**
   * 关闭.
   */
  void stop();

  /**
   * 获取注册数据.
   *
   * @param key 键
   * @return 值
   */
  String getData(String key);

  /**
   * 获取数据是否存在.
   *
   * @param key 键
   * @return 数据是否存在
   */
  boolean isExisted(String key);

  /**
   * 持久化注册数据.
   *
   * @param key 键
   * @param value 值
   */
  void persist(String key, String value);

  /**
   * 更新注册数据.
   *
   * @param key 键
   * @param value 值
   */
  void update(String key, String value);

  /**
   * 删除注册数据.
   *
   * @param key 键
   */
  void delete(String key);

  /**
   * 获取子节点名称集合.
   *
   * @param key 键
   * @return 子节点名称集合
   */
  List<String> getChildrenKeys(String key);

  /**
   * 获取子节点数量.
   *
   * @param key 键
   * @return 子节点数量
   */
  int getNumChildren(String key);

  /**
   * 添加连接状态的监听机制.
   */
  void addConnStateLister(ConnectionStateListener connStateListener);

  /**
   * 获取原生协调器的客户端.
   *
   * @return 注册中心的原生客户端
   */
  T getRawClient();
}
