package net.iwinter.core.annotation;

import net.iwinter.core.constant.HttpServerConstant;

import java.lang.annotation.*;

/**
 * @author: luogang
 * @email: iwinter997@gmail.com
 * @createDate: 2019/04/10 16:41
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value() default "";

    String responseType() default HttpServerConstant.ContentType.TEXT;
}
