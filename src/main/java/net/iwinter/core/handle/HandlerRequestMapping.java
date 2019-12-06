package net.iwinter.core.handle;

import net.iwinter.core.annotation.RequestController;
import net.iwinter.core.annotation.RequestMapping;
import net.iwinter.core.config.HttpServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: luogang
 * @email: iwinter997@gmail.com
 * @createDate: 2019/04/11 11:36
 */
public class HandlerRequestMapping {
    private final static Logger log = LoggerFactory.getLogger(HandlerRequestMapping.class);
    public static Map<String, InstanceParamMapping> requestUrlMap = null;

    /**
     * 获取扫描包路径
     *
     * @param clazz       加载类
     * @param basePackage 扫描地址
     */
    private static void scanPackage(Class clazz, String basePackage) throws Exception {
        URL url = clazz.getClassLoader().getResource(basePackage.replace(".", "/"));
        String pathfile = url.getFile();
        File file = new File(pathfile);
        String[] files = file.list();

        int initialCapacity = (Objects.nonNull(files) && files.length > 0 ? files.length : 0);
        List<String> packageNames = new ArrayList(initialCapacity);
        for (String path : files) {
            File eachFile = new File(pathfile + "/" + path);
            if (eachFile.isDirectory()) {
                scanPackage(clazz, basePackage + "." + eachFile.getName());
            } else {
                packageNames.add(basePackage + "." + eachFile.getName());
            }
        }
        filterAndInstance(packageNames);
    }

    /**
     * 获取实例
     *
     * @param packageNames 包路径
     * @throws Exception
     */
    private static void filterAndInstance(List<String> packageNames) throws Exception {
        if (Objects.isNull(packageNames) || packageNames.isEmpty()) {
            log.info("HandlerRequestMapping -> filterAndInstance() -> param is empty.");
            return;
        }
        Map<String, Object> instanceMap = new ConcurrentHashMap<>(packageNames.size());
        for (String classname : packageNames) {
            Class ccName = Class.forName(classname.replace(".class", ""));
            if (ccName.isAnnotationPresent(RequestController.class)) {
                Object instance = ccName.newInstance();
                RequestController an = (RequestController) ccName.getAnnotation(RequestController.class);
                String key = an.value();
                instanceMap.put(key, instance);
            }
            continue;
        }
        handlerMap(instanceMap);
    }

    /**
     * 映射请求地址
     *
     * @param instanceMap 实例集合
     */
    private static void handlerMap(Map<String, Object> instanceMap) {
        if (Objects.isNull(instanceMap) || instanceMap.isEmpty()) {
            log.info("HandlerRequestMapping -> handlerMap() -> param is empty.");
            return;
        }
        requestUrlMap = new ConcurrentHashMap<>(instanceMap.size());
        for (Map.Entry<String, Object> entry : instanceMap.entrySet()) {
            RequestController controller = entry.getValue().getClass().getAnnotation(RequestController.class);
            String ctvalue = controller.value();
            Method[] methods = entry.getValue().getClass().getMethods();
            for (Method method : methods) {
                if (!method.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                }
                InstanceParamMapping paramMapping = new InstanceParamMapping();
                paramMapping.setClazz(entry.getValue());
                paramMapping.setMethod(method);
                RequestMapping rm = method.getAnnotation(RequestMapping.class);
                requestUrlMap.put(ctvalue.concat(rm.value()), paramMapping);
            }
        }
    }

    /**
     * 处理请求地址
     *
     * @param clazz 启动类class
     * @throws Exception
     */
    public static void handelRequestUrl(Class clazz) throws Exception {
        scanPackage(clazz, HttpServerConfig.getScanPath());
    }
}