package org.timing.go.common.coordinator;

import java.util.List;
import org.apache.curator.framework.CuratorFramework;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 基于Zookeeper是的分布式协调注册中心. 使用开源的Zookeeper客户端 - Curator
 *
 * @author thinking_fioa 2020/1/5
 */
public class ZkCoordinator implements IDistributeCoordinator {

  private static final Logger LOGGER = LogManager.getLogger(ZkCoordinator.class);

  private CuratorFramework zkClient;

  public ZkCoordinator() {

  }

  @Override
  public void init() {
    LOGGER.info("connect and start zookeeper");
    // TODO 提供给配置文件
    // retry strategy，指数规避重试策略
//    RetryPolicy retryPolicy = new ExponentialBackoffRetry(zkConfig.getBaseSleepTimeMilliseconds(),
//        zkConfig.getMaxRetries(), zkConfig.getMaxSleepTimeMilliseconds());

    try {
      // crate zookeeper client
//      CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
//          .connectString(zkConfig.getServerLists()).retryPolicy(retryPolicy)
//          .namespace(zkConfig.getNamespace())
//          .sessionTimeoutMs(
//              1000 * Integer.parseInt(conf.getString(Constants.ZOOKEEPER_SESSION_TIMEOUT)))
//          .connectionTimeoutMs(
//              1000 * Integer.parseInt(conf.getString(Constants.ZOOKEEPER_CONNECTION_TIMEOUT)))
//          .build();

      zkClient.start();
    } catch (Exception cause) {
      LOGGER.warn("connect and start zookeeper.");
      throw cause;
    }
  }

  @Override
  public void start() {

  }

  @Override
  public void stop() {

  }

  @Override
  public String getData(String key) {
    return null;
  }

  @Override
  public boolean isExisted(String key) {
    return false;
  }

  @Override
  public void persist(String key, String value) {

  }

  @Override
  public void update(String key, String value) {

  }

  @Override
  public void delete(String key) {

  }

  @Override
  public List<String> getChildrenKeys(String key) {
    return null;
  }

  @Override
  public int getNumChildren(String key) {
    return 0;
  }

  @Override
  public void addLister() {

  }

  @Override
  public Object getRawClient() {
    return null;
  }
}
