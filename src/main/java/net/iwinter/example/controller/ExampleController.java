package net.iwinter.example.controller;

import net.iwinter.core.annotation.RequestController;
import net.iwinter.core.annotation.RequestMapping;
import net.iwinter.core.constant.HttpServerConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: luogang
 * @email: iwinter997@gmail.com
 * @createDate: 2019/04/12 19:45
 */
@SuppressWarnings("all")
@RequestController(value = "/example/http")
public class ExampleController {
    /**
     * 响应Txt
     *
     * @param param 请求参数
     * @return
     */
    @RequestMapping(value = "/text")
    public String resultTxt(String param) {
        return param;
    }

    /**
     * 响应Json
     *
     * @param param 请求参数
     * @return
     */
    @RequestMapping(value = "/json", responseType = HttpServerConstant.ContentType.JSON)
    public Map<String, Object> resultJson(String param) {
        Map<String, Object> resultMap = new HashMap<String, Object>(2) {{
            put("data", param);
            put("code", 200);
        }};
        return resultMap;
    }
}
