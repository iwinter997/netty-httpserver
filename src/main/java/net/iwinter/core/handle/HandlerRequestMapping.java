package net.iwinter.core.handle;

import net.iwinter.core.annotation.HttpRequestController;
import net.iwinter.core.annotation.HttpRequestMapping;
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

    private static List<String> packageNames = new ArrayList<>();
    private static Map<String, Object> handlerMap = new ConcurrentHashMap<>();
    private static Map<String, Object> instanceMap = new ConcurrentHashMap<>();

    /**
     * 获取扫描的包路径
     *
     * @param clazz
     * @param basePackage
     */
    private static void scanPackage(Class clazz, String basePackage) throws Exception {
        URL url = clazz.getClassLoader().getResource(replaceTo(basePackage));

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
        filterAndInstance();
    }

    private static String replaceTo(String path) {
        return path.replace(".", "/");
    }

    private static void filterAndInstance() throws Exception {
        if (packageNames.isEmpty()) {
            return;
        }
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
        handlerMap();
    }


    private static void handlerMap() {
        if (instanceMap.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : instanceMap.entrySet()) {
            if (entry.getValue().getClass().isAnnotationPresent(HttpRequestController.class)) {
                HttpRequestController controller = entry.getValue().getClass().getAnnotation(HttpRequestController.class);
                String ctvalue = controller.value();
                Method[] methods = entry.getValue().getClass().getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(HttpRequestMapping.class)) {
                        HttpRequestMapping rm = method.getAnnotation(HttpRequestMapping.class);
                        String rmvalue = rm.value();
                        handlerMap.put("/" + ctvalue + "/" + rmvalue, method);
                    } else {
                        continue;
                    }
                }
            } else {
                continue;
            }
        }
    }

    public static void handelRequestUrl(Class clazz) throws Exception {
        scanPackage(clazz, clazz.getPackage().getName());
    }
}
