package org.timing.go.executor.samples.zk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Resource;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.data.Stat;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.timing.go.common.util.GsonUtils;
import org.timing.go.common.util.StringUtils;
import org.timing.go.common.zkbean.MetaJobHttpZkBean;
import org.timing.go.executor.samples.SampleConstant;
import org.timing.go.executor.samples.cfg.MetaJobHttpCfg;

/**
 * 元数据注册中心.
 *
 * 当 Spring 加载完所有的Bean，会发送 {@link ContextRefreshedEvent}事件
 *
 * @author thinking_fioa 2020/4/26
 */
@Component
public class MetaJobZkRegister implements ApplicationListener<ContextRefreshedEvent> {

  private static final Logger LOGGER = LogManager.getLogger(MetaJobZkRegister.class);

  @Resource
  private MetaJobHttpCfg jobHttpCfg;

  private CuratorFramework zkClient;

  /**
   * 仅确保初始化一次.
   */
  private static final AtomicBoolean INIT_ONCE_ONLY = new AtomicBoolean(false);

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    if (INIT_ONCE_ONLY.compareAndSet(false, true)) {
      // 初始化Zk
      LOGGER.info("connect and start zookeeper. {}", jobHttpCfg.getZkQuorum());
      // retry strategy，指数规避重试策略
      RetryPolicy retryPolicy = new ExponentialBackoffRetry(jobHttpCfg.getZkBaseSleepTime(),
          jobHttpCfg.getZkMaxRetries(), jobHttpCfg.getZkRetryMaxSleepTime());

      try {
        // crate zookeeper client
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
            .connectString(jobHttpCfg.getZkQuorum()).retryPolicy(retryPolicy)
            .sessionTimeoutMs(jobHttpCfg.getZkSessionTimeoutMs())
            .connectionTimeoutMs(jobHttpCfg.getZkConnectionTimeoutMs());

        zkClient = builder.build();

        zkClient.start();

        // add connection state lister
        addConnStateLister((CuratorFramework client, ConnectionState newState) -> {
          LOGGER.info("conn state change to {}", newState.name());
          if (newState == ConnectionState.LOST) {
            LOGGER.info("connection lost {}.", jobHttpCfg.getZkQuorum());
          }
        });

        // 添加关闭的Hook
        addShutdownHook();
      } catch (Exception cause) {
        LOGGER.warn("connect and start zookeeper fail.");
        throw cause;
      }
    }
  }

  /**
   * 上传MetaHttpJob
   */
  public void upLoadMetaJobHttp(MetaJobHttpZkBean zkBean) {
    try {
      String projectZkPath = buildProjectZkPath(zkBean);
      if (!isExisted(projectZkPath)) {
        // 创建项目节点, 持久化节点.
        createPersistent(projectZkPath, SampleConstant.EMPTY_STR);
        LOGGER.info("create zk path {}. {}", projectZkPath, jobHttpCfg.getZkQuorum());
      }
      // 创建临时HttpPath节点
      String transformHttpUrl = StringUtils.transformUrl(zkBean.getHttpPath());
      String metaJobHttpPath = StringUtils.buildZkPath(projectZkPath, transformHttpUrl);
      if (!isExisted(metaJobHttpPath)) {
        createEphemeral(metaJobHttpPath, GsonUtils.toPrettyJson(zkBean));
      } else {
        LOGGER.info("{} existed.", metaJobHttpPath);
        // 应用重启时，可能存在临时节点来得及删除，添加监听器
        watchNodeDeleted(metaJobHttpPath, zkBean);
      }
    } catch (Exception cause) {
      LOGGER.warn("upload MetaJob failed.{}, {}, {}", zkBean.getAppName(),
          zkBean.getHttpPath(), zkBean.getMetaJobDesc(), cause);
    }
  }

  private boolean isExisted(String key) {
    try {
      return null != zkClient.checkExists().forPath(key);
    } catch (Exception cause) {
      LOGGER.info("key {} happen exception {}", key, cause);
      return false;
    }
  }

  private void create(String key, String value, CreateMode nodeMode) {
    try {
      if (!isExisted(key)) {
        zkClient.create().creatingParentsIfNeeded().withMode(nodeMode)
            .forPath(key, value.getBytes(SampleConstant.projectCharset()));
      } else {
        update(key, value);
      }
    } catch (Exception cause) {
      LOGGER.warn("key {} happen exception {}", key, cause);
    }
  }

  public void createPersistent(String key, String value) {
    create(key, value, CreateMode.PERSISTENT);
  }

  public void createEphemeral(String key, String value) {
    try {
      if (isExisted(key)) {
        zkClient.delete().deletingChildrenIfNeeded().forPath(key);
      }
      create(key, value, CreateMode.EPHEMERAL);
    } catch (Exception cause) {
      LOGGER.warn("key {} happen exception {}", key, cause);
    }
  }

  private void update(String key, String value) {
    try {
      zkClient.inTransaction().check().forPath(key).and().setData()
          .forPath(key, value.getBytes(SampleConstant.projectCharset())).and().commit();
    } catch (Exception cause) {
      LOGGER.warn("key {} happen exception {}", key, cause);
    }
  }

  public void delete(String key) {
    try {
      zkClient.delete().deletingChildrenIfNeeded().forPath(key);
    } catch (Exception cause) {
      LOGGER.warn("key {} happen exception {}", key, cause);
    }
  }

  public List<String> getChildrenKeys(String key) {
    try {
      return zkClient.getChildren().forPath(key);
    } catch (Exception cause) {
      LOGGER.warn("key {} happen exception {}", key, cause);
      return new ArrayList<>();
    }
  }

  public int getNumChildren(String key) {
    try {
      Stat stat = zkClient.checkExists().forPath(key);
      if (null != stat) {
        return stat.getNumChildren();
      }
      return 0;
    } catch (Exception cause) {
      LOGGER.warn("key {} happen exception {}", key, cause);
      return 0;
    }
  }

  private void close() {
    zkClient.getZookeeperClient().close();
    zkClient.close();
    LOGGER.info("zk {} close success.", jobHttpCfg.getZkQuorum());
  }

  private void addConnStateLister(ConnectionStateListener connStateListener) {
    if (zkClient == null) {
      LOGGER.warn("zkClient is null");
      return;
    }
    zkClient.getConnectionStateListenable().addListener(connStateListener);
  }

  private void addShutdownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        close();
      } catch (Exception cause) {
        LOGGER.warn("shutdownHook zk failed. {}", jobHttpCfg.getZkQuorum());
      }
    }));
    LOGGER.info("add shutdownHook {}", jobHttpCfg.getZkQuorum());
  }

  /**
   * 创建Http项目的Zk路径.
   *
   * /TimingGo/MetaJobHttp/Register/GroupName/AppName/Ip:Port
   */
  private String buildProjectZkPath(MetaJobHttpZkBean zkBean) {
    String ipAndPort = String.format("%s:%s", zkBean.getIp(), zkBean.getPort());

    return StringUtils.buildZkPath(jobHttpCfg.getZkRootNamespace(), jobHttpCfg.getZkSubNamespace(),
        SampleConstant.META_JOB_SOURCE, zkBean.getGroupName(), zkBean.getAppName(),
        ipAndPort);
  }

  private void watchNodeDeleted(String metaJobHttpPath, MetaJobHttpZkBean zkBean) {
    try {
      zkClient.getZookeeperClient().getZooKeeper()
          .exists(metaJobHttpPath, (WatchedEvent event) -> {
            if (event.getType().equals(EventType.NodeDeleted)) {
              createEphemeral(metaJobHttpPath, GsonUtils.toPrettyJson(zkBean));
            } else {
              // ZK的监听机制仅一次有效
              watchNodeDeleted(metaJobHttpPath, zkBean);
            }
          });
    } catch (Exception cause) {
      LOGGER.warn("upload MetaJob failed.{}, {}, {}", zkBean.getAppName(),
          zkBean.getHttpPath(), zkBean.getMetaJobDesc(), cause);
    }
  }
}
