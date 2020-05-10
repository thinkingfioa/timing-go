package org.timing.go.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * GSON 工具类.
 *
 * @author thinking_fioa 2020/4/12
 */
public class GsonUtils {

  private GsonUtils() {
    throw new UnsupportedOperationException("static class.");
  }

  private static final Gson GSON = new GsonBuilder().create();

  private static final Gson GSON_PRETTY = new GsonBuilder().setPrettyPrinting().create();

  /**
   * 对象转字符串.
   */
  public static String toPrettyString(Object obj) {
    return GSON_PRETTY.toJson(obj);
  }

  public static String toString(Object obj) {
    return GSON.toJson(obj);
  }

  /**
   * 字符串转成对象.
   */
  public static <T> T fromJson(String json, Class<T> clazz) {
    return GSON.fromJson(json, clazz);
  }
}
