package net.iwinter.core.annotation;

import java.lang.annotation.*;

/**
 * @author: luogang
 * @email: iwinter997@gmail.com
 * @createDate: 2019/04/10 16:40
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SuppressWarnings("all")
public @interface RequestController {
    String value() default "";
}
