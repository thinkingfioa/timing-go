package org.timing.go.common.util;

import java.util.List;
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

  public static boolean emptyList(List<?> list) {
    return null == list || list.isEmpty();
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
   * 更具Zk的路径地址，获取深度
   */
  public static int calDepthOfZkPath(String zkPath) {
    if (emptyCheck(zkPath)) {
      return 0;
    }

    int count = 0;
    char[] zkPathChars = zkPath.toCharArray();
    for (char single : zkPathChars) {
      if (single == CommonConstant.BACKSLASH_CHAR) {
        count++;
      }
    }
    return count;
  }


  /**
   * 以特殊关键字结尾.
   */
  public static boolean endsWith(String str, String suffix) {
    return !emptyCheck(str) && str.endsWith(suffix);
  }

  /**
   * 转换Url. 将 '／'替换为 '\'
   */
  public static String transformUrl(String url) {
    if (emptyCheck(url)) {
      return url;
    }
    return url.replace(CommonConstant.BACKSLASH, CommonConstant.SLASH);
  }

  /**
   * 以特殊关键字结尾.
   */
  public static boolean startsWith(String str, String suffix) {
    return !emptyCheck(str) && str.startsWith(suffix);
  }

  /**
   * 合并字符串.
   */
  public static String join(CharSequence delimiter, CharSequence... elements) {
    return String.join(delimiter, elements);
  }
}
