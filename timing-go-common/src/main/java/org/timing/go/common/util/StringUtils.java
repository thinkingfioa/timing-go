package org.timing.go.common.util;

/**
 * {@link String} 工具类.
 *
 * @author thinking_fioa 2020/4/12
 */
public class StringUtils {

  private StringUtils() {
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
