package org.timing.go.scheduler.core.zk;

import java.util.List;
import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.timing.go.common.util.GsonUtils;
import org.timing.go.common.util.StringUtils;
import org.timing.go.scheduler.SchedulerConstant;
import org.timing.go.scheduler.cfg.SchedulerCfg;
import org.timing.go.scheduler.service.MetaJobService;

/**
 * 监听Zookeeper元数据
 *
 * @author thinking_fioa 2020/5/9
 */
@Component
@Order(value = 3)
public class MetaJobHttpZkListener implements CommandLineRunner {

  private static final Logger LOGGER = LogManager.getLogger(MetaJobHttpZkListener.class);

  /**
   * IP:PORT 所在Zk路径深度
   */
  private int IP_PORT_NODE_DEPTH = 6;

  /**
   * HTTP_PATH 所在Zk路径深度
   */
  private int HTTP_PATH_NODE_DEPTH = 7;


  @Resource
  private MetaJobService metaJobService;

  @Resource
  private SchedulerZkOperator zkOperator;

  @Resource
  private SchedulerCfg schedulerCfg;

  @Override
  public void run(String... args) throws Exception {
    LOGGER.info("MetaJobZkListener starting");
    if (null == zkOperator) {
      LOGGER.warn("ZkOperator init failed.");
      return;
    }

    // 1. 构建Zookeeper路径，处理自动注册的MetaJobHttp
    String metaJobHttpRegisterZkPath = buildMetaJobHttpRegisterZkPath();
    handlerAndListenMetaJobHttp(metaJobHttpRegisterZkPath);

    // 2. 构建Zookeeper路径，处理自动注册的MetaJobHttp
    String metaJobHttpWebZkPath = buildMetaJobHttpWebZkPath();
    handlerAndListenMetaJobHttp(metaJobHttpWebZkPath);
  }


  /**
   * 处理并监听新添加的元Job
   */
  private void handlerAndListenMetaJobHttp(String zkPath) {
    LOGGER.info("handler and listen MetaJobHttp {}", zkPath);
    handleMetaJobHttp(zkPath);
    listenMetaJobHttp(zkPath);
  }


  /**
   * 将Zk路径下的MetaJobHttp存入数据库中
   */
  private void handleMetaJobHttp(String zkPath) {
    int depth = StringUtils.calDepthOfZkPath(zkPath);
    LOGGER.debug("zkPath {} depth {}", zkPath, depth);

    // 递归进入IP:PORT节点层
    if (depth < IP_PORT_NODE_DEPTH) {
      List<String> childList = zkOperator.getZkCoordinator().getChildrenKeys(zkPath);
      if (CommonUtils.emptyCheck(childList)) {
        return;
      }

      for (String child : childList) {
        String childPath = StringUtils.buildZkPath(zkPath, child);
        handleMetaJobHttp(childPath);
      }
      return;
    }

    // 达到IP:PORT节点层
    LOGGER.info("arrive IP:PORT Node. zkPath {} depth {}", zkPath, depth);
    List<String> childList = zkOperator.getZkCoordinator().getChildrenKeys(zkPath);
    if (CommonUtils.emptyCheck(childList)) {
      return;
    }
    for (String child : childList) {
      String childPath = StringUtils.buildZkPath(zkPath, child);
      // 处理节点
      dealWithMethJob(childPath, child);
      LOGGER.info("handler MetaJobHttp. childPath {} zkPath {} depth {}", childPath, zkPath, depth);
    }
  }

  /**
   * 监听Zk路径下的MetaJobHttp，并存入数据库中
   */
  private void listenMetaJobHttp(String zkPath) {

  }

  /**
   * 处理元Job
   *
   * @param zkPath /TimingGo/MetaJobHttp/{Web, Register}/GroupName/AppName/IP:PORT/HttpPath
   * @param httpPath HttpPath节点
   */
  private void dealWithMethJob(String zkPath, String httpPath) {
    if (CommonUtils.emptyCheck(zkPath, httpPath)) {
      LOGGER.warn("found empty node. zkPath {}, httpPath {}", zkPath, httpPath);
      return;
    }

    String metaJobData = zkOperator.getZkCoordinator().getData(zkPath);
    if (CommonUtils.emptyCheck(metaJobData)) {
      LOGGER.warn("MetaJobHttp data empty. zkPath {}, httpPath {}", zkPath, httpPath);
      return;
    }
    MetaJobHttpZkBean metaJobHttpBean = GsonUtils.fromJson(metaJobData, MetaJobHttpZkBean.class);

  }

  /**
   * 构建元Job，自动注册路径. ／TimingGo／MetaJobHttp／Register
   */
  private String buildMetaJobHttpRegisterZkPath() {
    return StringUtils.buildZkPath(schedulerCfg.getZkRootNamespace(),
        schedulerCfg.getMetaJobHttpZkSubNs(), SchedulerConstant.META_JOB_SOURCE_REGISTER);
  }

  /**
   * 构建元Job，通过Web添加的路径. ／TimingGo／MetaJobHttp／Web
   */
  private String buildMetaJobHttpWebZkPath() {
    return StringUtils.buildZkPath(schedulerCfg.getZkRootNamespace(),
        schedulerCfg.getMetaJobHttpZkSubNs(), SchedulerConstant.META_JOB_SOURCE_WEB);
  }
}
