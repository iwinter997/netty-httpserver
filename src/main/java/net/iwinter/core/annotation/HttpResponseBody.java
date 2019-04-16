package net.iwinter.core.annotation;

import net.iwinter.core.constant.HttpServerConstant;

import java.lang.annotation.*;

/**
 * @author: luogang
 * @email: iwinter997@gmail.com
 * @createDate: 2019/04/16 14:13
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HttpResponseBody {
    String value() default HttpServerConstant.ContentType.TEXT;
}
