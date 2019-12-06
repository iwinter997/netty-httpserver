package net.iwinter.core.utils;

import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author: luogang
 * @email: iwinter997@gmail.com
 * @createDate: 2019/04/16 20:49
 */
public class PropertiesUtil {
    private Properties props = null;

    public PropertiesUtil(String fileName) {
        readProperties(fileName);
    }

    /**
     * 加载配置文件
     *
     * @param fileName 文件名称
     */
    private void readProperties(String fileName) {
        try (InputStreamReader inputStream = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");) {
            props = new Properties();
            props.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return props.getProperty(key);
    }
}
