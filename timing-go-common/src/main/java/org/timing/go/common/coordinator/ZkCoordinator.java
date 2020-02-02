package org.timing.go.common.coordinator;

import java.util.List;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 基于Zookeeper是的分布式协调注册中心. 使用开源的Zookeeper客户端 - Curator
 *
 * @author thinking_fioa 2020/1/5
 */
public class ZkCoordinator implements IDistributeCoordinator {

  private static final Logger LOGGER = LogManager.getLogger(ZkCoordinator.class);

  private IZkCfg zkCfg;

  private CuratorFramework zkClient;

  public ZkCoordinator(IZkCfg zkCfg) {
    this.zkCfg = zkCfg;
  }

  @Override
  public void init() {
    LOGGER.info("connect and start zookeeper. {}", zkCfg.getZkQuorum());
    // retry strategy，指数规避重试策略
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(zkCfg.getZkRetryBaseSleepTime(),
        zkCfg.getZkMaxRetries(), zkCfg.getZkRetryMaxSleepTime());

    try {
      // crate zookeeper client
      CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
          .connectString(zkCfg.getZkQuorum()).retryPolicy(retryPolicy)
          .namespace(zkCfg.getNamespace())
          .sessionTimeoutMs(zkCfg.getSessionTimemoutMs())
          .connectionTimeoutMs(zkCfg.getConnectionTimeoutMs());

      zkClient = builder.build();

      zkClient.start();
      // add connection state lister
      addConnStateLister((CuratorFramework client, ConnectionState newState) -> {
        LOGGER.info("conn state change to {}", newState.name());
        if (newState == ConnectionState.LOST) {
          LOGGER.info("connection lost.");
        }
      });
    } catch (Exception cause) {
      LOGGER.warn("connect and start zookeeper.");
      throw cause;
    }
  }

  @Override
  public void start() {
    zkClient.start();
    LOGGER.info("zookeeper start.");
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
  public void addConnStateLister(ConnectionStateListener connStateListener) {
    if (zkClient == null) {
      LOGGER.warn("zkClient is null");
      return;
    }
    zkClient.getConnectionStateListenable().addListener(connStateListener);
  }

  @Override
  public Object getRawClient() {
    return zkClient;
  }
}
