package net.iwinter.example.controller;

import net.iwinter.core.annotation.HttpRequestController;
import net.iwinter.core.annotation.HttpRequestMapping;
import net.iwinter.core.annotation.HttpResponseBody;
import net.iwinter.core.constant.HttpServerConstant;

/**
 * @author: luogang
 * @email: iwinter997@gmail.com
 * @createDate: 2019/04/12 19:45
 */

@HttpRequestController(value = "/test")
public class TestController {

    @HttpRequestMapping(value = "/text")
    @HttpResponseBody
    public String textValue(String param) {
        return param;
    }


    @HttpRequestMapping(value = "/json")
    @HttpResponseBody(value = HttpServerConstant.ContentType.JSON)
    public String jsonValue(String param) {
        return param;
    }
}
