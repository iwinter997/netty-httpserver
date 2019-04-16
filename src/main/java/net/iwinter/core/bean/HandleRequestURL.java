package net.iwinter.core.bean;

import java.lang.reflect.Method;

/**
 * @author: luogang
 * @email: iwinter997@gmail.com
 * @createDate: 2019/04/12 20:59
 */
public class HandleRequestURL {
    private Method method;
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
