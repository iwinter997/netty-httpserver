package net.iwinter.core.constant;

/**
 * @author: luogang
 * @email: iwinter997@gmail.com
 * @createDate: 2019/04/16 13:41
 */
public final class HttpServerConstant {
    /**
     * 配置文件名称
     */
    public final static String HTTP_SERVER_CONFIG_FILE = "config.properties";
    /**
     * 服务端口
     */
    public final static String HTTP_SERVER_PORT = "http.server.port";

    /**
     * 响应体类型
     */
    public static class ContentType {
        public final static String JSON = "application/json; charset=UTF-8";
        public final static String TEXT = "text/plain; charset=UTF-8";
        public final static String HTML = "text/html; charset=UTF-8";
    }
}
