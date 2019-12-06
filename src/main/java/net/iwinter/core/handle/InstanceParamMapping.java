package net.iwinter.core.handle;

import java.lang.reflect.Method;

/**
 * Created by luogang on 2019/12/06 16:47
 */
public class InstanceParamMapping {
    /**
     * 被@RequestMapping修饰的方法
     */
    private Method method;
    /**
     * 被@RequestController修饰的实例
     */
    private Object clazz;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getClazz() {
        return clazz;
    }

    public void setClazz(Object clazz) {
        this.clazz = clazz;
    }
}
