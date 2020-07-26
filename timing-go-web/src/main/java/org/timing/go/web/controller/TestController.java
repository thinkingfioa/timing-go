package org.timing.go.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.timing.go.web.service.TestService;
import org.timing.go.web.util.ResultT;

/**
 * @author thinking_fioa 2020/2/9
 */
@Api(tags = "TEST_TAG")
@RestController
@RequestMapping("/test")
public class TestController {

  private static final Logger LOGGER = LogManager.getLogger(TestController.class);

  @Resource
  private TestService testService;

  @ApiOperation(value = "queryUserList", notes = "QUERY_USER_LIST_NOTES")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "name", value = "luweilin", required = true, type = "String")
  })
  @GetMapping(value = "/testName")
  @ResponseStatus(HttpStatus.OK)
  public ResultT queryUserList(@RequestParam("name") String name) {
    try {
      LOGGER.info("test11111111111 {}", name);
      return new ResultT(200, "luweilini");
    } catch (Exception cause) {
      LOGGER.error("", cause);
      throw cause;
    }
  }
}
