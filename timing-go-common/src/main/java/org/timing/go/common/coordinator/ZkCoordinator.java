package org.timing.go.common.coordinator;

import java.util.ArrayList;
import java.util.List;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

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
    zkClient.getZookeeperClient().close();
    zkClient.close();
    LOGGER.info("zookeeper close {}", zkCfg.getZkQuorum());
  }

  @Override
  public String getData(String key) {
    try {
      return new String(zkClient.getData().forPath(key), zkCfg.dataCharset());
    } catch (Exception cause) {
      LOGGER.info("key {} happen exception {}", key, cause);
      return null;
    }
  }

  @Override
  public boolean isExisted(String key) {
    try {
      return null != zkClient.checkExists().forPath(key);
    } catch (Exception cause) {
      LOGGER.info("key {} happen exception {}", key, cause);
      return false;
    }
  }

  @Override
  public void create(String key, String value, CreateMode nodeMode) {
    try {
      if (!isExisted(key)) {
        zkClient.create().creatingParentsIfNeeded().withMode(nodeMode)
            .forPath(key, value.getBytes(zkCfg.dataCharset()));
      } else {
        update(key, value);
      }
    } catch (Exception cause) {
      LOGGER.info("key {} happen exception {}", key, cause);
    }
  }

  @Override
  public void createPersistent(String key, String value) {
    create(key, value, CreateMode.PERSISTENT);
  }

  @Override
  public void createEphemeral(String key, String value) {
    try {
      if (isExisted(key)) {
        zkClient.delete().deletingChildrenIfNeeded().forPath(key);
      }
      create(key, value, CreateMode.EPHEMERAL);
    } catch (Exception cause) {
      LOGGER.info("key {} happen exception {}", key, cause);
    }
  }

  @Override
  public void createPersistentSequential(String key, String value) {
    create(key, value, CreateMode.PERSISTENT_SEQUENTIAL);
  }

  @Override
  public void createEphemeralSequential(String key, String value) {
    create(key, value, CreateMode.EPHEMERAL_SEQUENTIAL);
  }

  @Override
  public void update(String key, String value) {
    try {
      zkClient.inTransaction().check().forPath(key).and().setData()
          .forPath(key, value.getBytes(zkCfg.dataCharset())).and().commit();
    } catch (Exception cause) {
      LOGGER.info("key {} happen exception {}", key, cause);
    }
  }

  @Override
  public void delete(String key) {
    try {
      zkClient.delete().deletingChildrenIfNeeded().forPath(key);
    } catch (Exception cause) {
      LOGGER.info("key {} happen exception {}", key, cause);
    }
  }

  @Override
  public List<String> getChildrenKeys(String key) {
    try {
      return zkClient.getChildren().forPath(key);
    } catch (Exception cause) {
      LOGGER.info("key {} happen exception {}", key, cause);
      return new ArrayList<>();
    }
  }

  @Override
  public int getNumChildren(String key) {
    try {
      Stat stat = zkClient.checkExists().forPath(key);
      if (null != stat) {
        return stat.getNumChildren();
      }
      return 0;
    } catch (Exception cause) {
      LOGGER.info("key {} happen exception {}", key, cause);
      return 0;
    }
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
