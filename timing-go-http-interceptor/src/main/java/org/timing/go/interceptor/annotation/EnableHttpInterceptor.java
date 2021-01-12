package org.timing.go.interceptor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import org.timing.go.interceptor.HttpInterceptorListener;

/**
 * 启用Http 接口的拦截器
 * @author thinking_fioa 2021/1/12
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(value= {
    HttpInterceptorListener.class
})
public @interface EnableHttpInterceptor {

}
