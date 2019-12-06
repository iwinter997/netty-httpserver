package net.iwinter.core.config;

import net.iwinter.core.constant.HttpServerConstant;
import net.iwinter.core.utils.PropertiesUtil;
import org.apache.commons.lang.StringUtils;

/**
 * @author: luogang
 * @email: iwinter997@gmail.com
 * @createDate: 2019/04/10 16:26
 */
public final class HttpServerConfig {
    private static Integer serverPort;
    private static String scanPath;

    static {
        PropertiesUtil propertiesUtil = new PropertiesUtil(HttpServerConstant.HTTP_SERVER_CONFIG_FILE);
        String serverPort = propertiesUtil.get(HttpServerConstant.HTTP_SERVER_PORT).trim();
        if (!StringUtils.isEmpty(serverPort)) {
            setServerPort(Integer.parseInt(serverPort));
        }
    }

    private HttpServerConfig() {
    }

    public static Integer getServerPort() {
        return serverPort;
    }

    public static void setServerPort(Integer serverPort) {
        HttpServerConfig.serverPort = serverPort;
    }

    public static String getScanPath() {
        return scanPath;
    }

    public static void setScanPath(String scanPath) {
        HttpServerConfig.scanPath = scanPath;
    }
}
