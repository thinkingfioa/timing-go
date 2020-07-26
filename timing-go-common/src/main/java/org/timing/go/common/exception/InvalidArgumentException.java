package org.timing.go.common.exception;

/**
 * 不合法参数。
 *
 * @author thinking_fioa 2020/4/26
 */
public class InvalidArgumentException extends RuntimeException {

  private static final long serialVersionUID = 489520708871524298L;

  public InvalidArgumentException(String message) {
    super(message);
  }
}
