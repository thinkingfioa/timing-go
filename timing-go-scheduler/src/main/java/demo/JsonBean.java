package demo;

import java.util.List;

/**
 * @author thinking_fioa 2020/11/15
 */
public class JsonBean {

  private String templateId;

  private List<Bean> variables;

  public static class Bean {

    private String name;
    private String value;
  }
}
