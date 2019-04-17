package net.iwinter.example.controller;

import net.iwinter.core.annotation.HttpRequestController;
import net.iwinter.core.annotation.HttpRequestMapping;
import net.iwinter.core.annotation.HttpResponseBody;
import net.iwinter.core.constant.HttpServerConstant;

import java.util.HashMap;
import java.util.Map;

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
    public Map<String,Object> jsonValue(String param) {
        Map<String,Object> jsonMap = new HashMap<>(2);
        jsonMap.put("data",param);
        jsonMap.put("code",200);
        return jsonMap;
    }
}
