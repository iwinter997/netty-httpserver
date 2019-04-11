package net.iwinter.core.annotation;

import java.lang.annotation.*;

/**
 * @author: luogang
 * @email: iwinter997@gmail.com
 * @createDate: 2019/04/10 16:41
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HttpRequestMapping {
    String value() default "";
}
