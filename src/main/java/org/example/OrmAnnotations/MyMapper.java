package org.example.OrmAnnotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ziyuan
 * @since 2023.10
 */
@Target({ElementType.PARAMETER,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MyMapper {
    String value() default "";
}
