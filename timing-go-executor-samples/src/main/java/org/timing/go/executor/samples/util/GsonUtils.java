package org.timing.go.executor.samples.util;

import com.google.gson.Gson;

/**
 * @author thinking_fioa 2020/4/12
 */
public class GsonUtils {

  private static final Gson GSON = new Gson();

  private GsonUtils() {
    throw new UnsupportedOperationException("static class");
  }


  public static String toString(Object obj) {
    return GSON.toJson(obj);
  }
}
