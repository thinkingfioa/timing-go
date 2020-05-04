package org.timing.go.executor.samples;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 常量.
 *
 * @author thinking_fioa 2020/4/26
 */
public class SampleConstant {

  private SampleConstant() {
    throw new UnsupportedOperationException("static class");
  }

  /**
   * 项目编码方式.
   */
  public static Charset projectCharset() {
    return StandardCharsets.UTF_8;
  }

  public static String projectCharsetStr() {
    return StandardCharsets.UTF_8.name();
  }

  /**
   * 反斜杠.
   */
  public static final String BACKSLASH = "/";

  /**
   * 斜杠.
   */
  public static final String SLASH = "\\";

  public static final String EMPTY_STR = "";

  /**
   * 元Job来源方式。自动注册.
   */
  public static final String META_JOB_SOURCE = "Register";
}
