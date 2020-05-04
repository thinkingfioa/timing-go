package org.timing.go.executor.samples.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import org.timing.go.executor.samples.SampleConstant;
import org.timing.go.executor.samples.exception.InvalidArgumentException;

/**
 * @author thinking_fioa 2020/4/26
 */
public class CommonUtils {

  private static final Gson GSON = new GsonBuilder().create();

  private static final Gson GSON_PRETTY = new GsonBuilder().setPrettyPrinting().create();

  private CommonUtils() {
    throw new UnsupportedOperationException("static class");
  }

  /**
   * 字符串空检查.
   */
  public static boolean emptyCheck(String str) {
    return null == str || str.isEmpty();
  }

  public static boolean emptyList(List<?> list) {
    return null == list || list.isEmpty();
  }

  /**
   * 生成Zk路径.
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
      if (!startsWith(name, SampleConstant.BACKSLASH)) {
        sb.append(SampleConstant.BACKSLASH).append(name);
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

  /**
   * 转换Url. 将 '／'替换为 '\'
   */
  public static String transformUrl(String url) {
    if (emptyCheck(url)) {
      return url;
    }
    return url.replace(SampleConstant.BACKSLASH, SampleConstant.SLASH);
  }

  public static boolean equalAfterTrim(String str1, String str2) {
    return (str1 == null && str2 == null)
        || (str1 != null && str2 != null && str1.trim().equals(str2.trim()));
  }

  public static String toJson(Object obj) {
    return GSON_PRETTY.toJson(obj);
  }

}
