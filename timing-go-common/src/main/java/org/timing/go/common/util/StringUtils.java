package org.timing.go.common.util;

import org.timing.go.common.CommonConstant;
import org.timing.go.common.exception.InvalidArgumentException;

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

  /**
   * 构建Zookeeper路径地址.
   */
  public static String buildZkPath(String... nameList) {
    if (null == nameList || nameList.length == 0) {
      throw new InvalidArgumentException("found empty nameList");
    }
    StringBuilder sb = new StringBuilder();
    for (String name : nameList) {
      if (emptyCheck(name)) {
        throw new InvalidArgumentException("found empty str");
      }
      if (!startsWith(name, CommonConstant.BACKSLASH)) {
        sb.append(CommonConstant.BACKSLASH).append(name);
      } else {
        sb.append(name);
      }
    }

    return sb.toString();
  }


  /**
   * 以特殊关键字结尾.
   */
  public static boolean endsWith(String str, String suffix) {
    return !emptyCheck(str) && str.endsWith(suffix);
  }

  /**
   * 以特殊关键字结尾.
   */
  public static boolean startsWith(String str, String suffix) {
    return !emptyCheck(str) && str.startsWith(suffix);
  }
}
