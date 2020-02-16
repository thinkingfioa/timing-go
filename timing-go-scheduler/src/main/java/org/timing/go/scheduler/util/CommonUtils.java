package org.timing.go.scheduler.util;

/**
 * 工具类.
 *
 * @author thinking_fioa 2020/2/16
 */
public class CommonUtils {

  private CommonUtils() {
    throw new UnsupportedOperationException("static class.");
  }

  public static boolean emptyCheck(String str) {
    return null == str || str.trim().isEmpty();
  }

  /**
   * 两个字符串trim后比较.
   */
  public static boolean equalAfterTrim(String str1, String str2) {
    return (str1 == null && str2 == null)
        || (str1 != null && str2 != null && str1.trim().equals(str2.trim()));
  }
}
