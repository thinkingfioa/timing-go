package org.timing.go.executor.samples.http.controller;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.timing.go.executor.samples.util.GsonUtils;

/**
 * Controller的案例.
 *
 * @author thinking_fioa 2020/4/12
 */
@Controller
public class HttpController {

  private static final Logger LOGGER = LogManager.getLogger(HttpController.class);

  @PostMapping(value = "/httpJob1", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String httpJob1() {
    Map<String, String> info = new HashMap<String, String>();
    info.put("result", "success-noparam");
    info.put("status", "success");
    LOGGER.info("httpJob1 executor success.");

    return GsonUtils.toString(info);
  }
}
