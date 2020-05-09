package org.timing.go.scheduler.core.zk;

import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
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

    // 构建Zookeeper路径.
    String metaJobHttpRegisterZkPath = buildMetaJobHttpRegisterZkPath();
    String metaJobHttpWebZkPath = buildMetaJobHttpWebZkPath();
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


  /**
   * 将Zk路径下的MetaJobHttp存入数据库中
   */
  private void handlerMetaJobHttp() {

  }

  /**
   * 监听Zk路径下的MetaJobHttp，并存入数据库中
   */
  private void listenMetaJobHttp() {

  }
}
