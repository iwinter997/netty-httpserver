package net.iwinter.core.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: luogang
 * @email: iwinter997@gmail.com
 * @createDate: 2019/04/10 16:26
 */
public class HttpServerConfig {
    private static HttpServerConfig httpServerConfig;

    private HttpServerConfig() {

    }

    public static HttpServerConfig getInstance() {
        if (null == httpServerConfig) {
            synchronized (HttpServerConfig.class) {
                if (null == httpServerConfig) {
                    Map<String, Object> configPeoperties = getConfigPeoperties();
                    httpServerConfig = new HttpServerConfig();
                    if (configPeoperties.containsKey("serverPort")) {
                        httpServerConfig.setServerPort((Integer) configPeoperties.get("serverPort"));
                    }
                }
            }
        }
        return httpServerConfig;
    }

    private static Map<String, Object> getConfigPeoperties() {
        Map<String, Object> serverConfigMap = new HashMap<String, Object>(1);

        // TODO 从配置文件加载
        serverConfigMap.put("serverPort", 8844);
        return serverConfigMap;
    }

    public Integer serverPort;
    public String scanPath;

    public static HttpServerConfig getHttpServerConfig() {
        return httpServerConfig;
    }

    public static void setHttpServerConfig(HttpServerConfig httpServerConfig) {
        HttpServerConfig.httpServerConfig = httpServerConfig;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getScanPath() {
        return scanPath;
    }

    public void setScanPath(String scanPath) {
        this.scanPath = scanPath;
    }
}
