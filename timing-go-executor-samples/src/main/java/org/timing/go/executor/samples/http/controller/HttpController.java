package org.timing.go.executor.samples.http.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.timing.go.common.CommonConstant;
import org.timing.go.common.util.GsonUtils;
import org.timing.go.common.util.StringUtils;
import org.timing.go.common.zkbean.MetaJobHttpTypeEnum;
import org.timing.go.common.zkbean.MetaJobHttpZkBean;
import org.timing.go.common.zkbean.MetaJobSourceEnum;
import org.timing.go.common.zkbean.MetaJobTypeEnum;
import org.timing.go.executor.samples.cfg.ApplicationCfg;
import org.timing.go.executor.samples.cfg.MetaJobHttpCfg;
import org.timing.go.executor.samples.zk.MetaJobZkRegister;

/**
 * Controller的案例.
 *
 * @author thinking_fioa 2020/4/12
 */
@RestController
public class HttpController {

  private static final Logger LOGGER = LogManager.getLogger(HttpController.class);

  @Resource
  private MetaJobZkRegister zkRegister;

  @Resource
  private ApplicationCfg appCfg;

  @Resource
  private MetaJobHttpCfg jobHttpCfg;

  @GetMapping(value = "/httpJob1", produces = "application/json;charset=UTF-8")
  public String httpJob1() {
    Map<String, String> info = new HashMap<>();
    info.put("result", "success-noparam");
    info.put("status", "success");
    LOGGER.info("httpJob1 executor success.");

    // upload
    String metaJobKey = StringUtils.join(CommonConstant.PLUS, appCfg.getAppName(), "/httpJob1");
    MetaJobHttpZkBean zkBean = new MetaJobHttpZkBean(metaJobKey, jobHttpCfg.getGroupName(), "测试方法",
        MetaJobTypeEnum.HTTP, MetaJobSourceEnum.REGISTER, appCfg.getAppName(),
        appCfg.getServerAddress(), appCfg.getServerPort(), "/httpJob1", MetaJobHttpTypeEnum.GET,
        HttpController.class, "httpJob1", String.class, new ArrayList<>());
    zkRegister.upLoadMetaJobHttp(zkBean);

    return GsonUtils.toPrettyJson(info);
  }
}
