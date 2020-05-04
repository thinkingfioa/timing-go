package org.timing.go.executor.samples.http.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.timing.go.executor.samples.cfg.ApplicationCfg;
import org.timing.go.executor.samples.cfg.MetaJobHttpCfg;
import org.timing.go.executor.samples.entity.MetaJobHttpEntity;
import org.timing.go.executor.samples.entity.MetaJobHttpEntity.HttpTypeEnum;
import org.timing.go.executor.samples.util.GsonUtils;
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
    MetaJobHttpEntity jobHttpEntity = new MetaJobHttpEntity(jobHttpCfg.getGroupName(),
        appCfg.getAppName(), appCfg.getServerAddress(), appCfg.getServerPort(), "/httpJob1",
        HttpTypeEnum.GET, HttpController.class, "httpJob1", String.class, new ArrayList<>(),
        "测试方法");
    zkRegister.upLoadMetaJobHttp(jobHttpEntity);

    return GsonUtils.toString(info);
  }
}
