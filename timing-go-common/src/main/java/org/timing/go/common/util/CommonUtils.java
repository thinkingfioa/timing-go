package org.timing.go.common.util;

import java.util.Collection;

/**
 * 工具类.
 *
 * @author thinking_fioa 2020/2/16
 */
public class CommonUtils {

  private CommonUtils() {
    throw new UnsupportedOperationException("static class.");
  }

  /**
   * 字符串空判断.
   */
  public static boolean emptyCheck(String str) {
    return null == str || str.trim().isEmpty();
  }

  /**
   * 任何一个字符串空，返回true
   */
  public static boolean emptyCheck(String... strs) {
    if (null == strs || strs.length == 0) {
      return true;
    }

    for (String str : strs) {
      if (emptyCheck(str)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 集合空判断.
   */
  public static boolean emptyCheck(Collection<?> collections) {
    return null == collections || collections.isEmpty();
  }

  /**
   * 两个字符串trim后比较.
   */
  public static boolean equalAfterTrim(String str1, String str2) {
    return (str1 == null && str2 == null)
        || (str1 != null && str2 != null && str1.trim().equals(str2.trim()));
  }
}
