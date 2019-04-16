package net.iwinter.core.handle;

import net.iwinter.core.annotation.HttpRequestController;
import net.iwinter.core.annotation.HttpRequestMapping;
import net.iwinter.core.bean.HandleRequestURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: luogang
 * @email: iwinter997@gmail.com
 * @createDate: 2019/04/11 11:36
 */
public class HandlerRequestMapping {
    private final static Logger log = LoggerFactory.getLogger(HandlerRequestMapping.class);

    public static Map<String, HandleRequestURL> requestUrlMap = new ConcurrentHashMap<>();

    /**
     * 获取扫描的包路径
     *
     * @param clazz
     * @param basePackage
     */
    private static void scanPackage(Class clazz, String basePackage) throws Exception {
        List<String> packageNames = new ArrayList<>();

        URL url = clazz.getClassLoader().getResource(basePackage.replace(".", "/"));
        String pathfile = url.getFile();
        File file = new File(pathfile);
        String[] files = file.list();
        for (String path : files) {
            File eachFile = new File(pathfile + "/" + path);
            if (eachFile.isDirectory()) {
                scanPackage(clazz, basePackage + "." + eachFile.getName());
            } else {
                packageNames.add(basePackage + "." + eachFile.getName());
            }
        }

        // 获取Class
        filterAndInstance(packageNames);
    }

    private static void filterAndInstance(List<String> packageNames) throws Exception {
        if (packageNames.isEmpty()) {
            return;
        }
        Map<String, Object> instanceMap = new ConcurrentHashMap<>();
        for (String classname : packageNames) {
            Class ccName = Class.forName(classname.replace(".class", ""));
            if (ccName.isAnnotationPresent(HttpRequestController.class)) {
                Object instance = ccName.newInstance();
                HttpRequestController an = (HttpRequestController) ccName.getAnnotation(HttpRequestController.class);
                String key = an.value();
                instanceMap.put(key, instance);
            }
            continue;
        }
        // 映射请求地址
        handlerMap(instanceMap);
    }


    private static void handlerMap(Map<String, Object> instanceMap) {
        if (instanceMap.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : instanceMap.entrySet()) {
            HttpRequestController controller = entry.getValue().getClass().getAnnotation(HttpRequestController.class);
            String ctvalue = controller.value();
            Method[] methods = entry.getValue().getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(HttpRequestMapping.class)) {
                    HttpRequestMapping rm = method.getAnnotation(HttpRequestMapping.class);

                    HandleRequestURL requestURL = new HandleRequestURL();
                    requestURL.setClazz(entry.getValue());
                    requestURL.setMethod(method);

                    requestUrlMap.put(ctvalue + rm.value(), requestURL);
                } else {
                    continue;
                }
            }

        }
    }

    public static void handelRequestUrl(Class clazz) throws Exception {
        scanPackage(clazz, clazz.getPackage().getName());
    }
}
