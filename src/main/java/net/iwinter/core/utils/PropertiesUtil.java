package net.iwinter.core.utils;

import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author: luogang
 * @email: iwinter997@gmail.com
 * @createDate: 2019/04/16 20:49
 */
public class PropertiesUtil {
    private Properties props;

    public PropertiesUtil(String fileName) {
        readProperties(fileName);
    }

    /**
     * 加载配置文件
     *
     * @param fileName
     */
    private void readProperties(String fileName) {
        try {
            props = new Properties();
            InputStreamReader inputStream = new InputStreamReader(
                    this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
            props.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return props.getProperty(key);
    }

    public Map<?, ?> getAll() {
        Map<String, String> map = new HashMap<>();
        Enumeration<?> enu = props.propertyNames();
        while (enu.hasMoreElements()) {
            String key = (String) enu.nextElement();
            String value = props.getProperty(key);
            map.put(key, value);
        }
        return map;
    }

}
