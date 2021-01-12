package org.timing.go.interceptor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记需要拦截的http方法
 *
 * @author thinking_fioa 2021/1/12
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface HttpInterceptor {

  /**
   * 描述
   */
  String desc() default "";
}
