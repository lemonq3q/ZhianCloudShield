package org.example.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER,ElementType.METHOD})  //注解是方法及的
@Retention(RetentionPolicy.RUNTIME)   //注解在运行阶段执行
@Documented  //生成文档
public @interface SystemControllerLog {
    String operation() default "get";
    String type() default "user";
}
