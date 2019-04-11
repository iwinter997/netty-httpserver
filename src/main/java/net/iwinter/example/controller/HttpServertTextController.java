package net.iwinter.example.controller;

import net.iwinter.core.annotation.HttpRequestController;
import net.iwinter.core.annotation.HttpRequestMapping;

/**
 * @author: luogang
 * @email: iwinter997@gmail.com
 * @createDate: 2019/04/10 16:43
 */
@HttpRequestController(value = "/text")
public class HttpServertTextController {

    @HttpRequestMapping(value = "/value")
    public String value(String value) {
        return value;
    }
}
