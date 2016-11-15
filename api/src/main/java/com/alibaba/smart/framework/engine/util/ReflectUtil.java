/**
 *
 */
package com.alibaba.smart.framework.engine.util;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

/**
 * @author dongdong.zdd
 */
public class ReflectUtil {

    public static Method getMethod(Object obj, String methodName, Class<?>[] parameterTypes) {
        Method[] methods = obj.getClass().getMethods();
        for (Method m : methods) {
            if (methodName.equals(m.getName())) {
                if (parameterTypes == null) {
                    return m;
                } else if (compare(m.getParameterTypes(), parameterTypes, true)) {
                    return m;
                }
            }
        }
        return null;
    }

    public static boolean compare(Class<?>[] c1, Class<?>[] c2, boolean matchOnObject) {
        if (c1.length != c2.length) {
            return false;
        }
        for (int i = 0; i < c1.length; i++) {
            if ((c1[i] == null) || (c2[i] == null)) {
                return false;
            }
            if (c1[i].equals(Object.class) && !matchOnObject) {
                return false;
            }
            if (!c1[i].isAssignableFrom(c2[i])) {
                return false;
            }
        }
        return true;
    }


    /**
     * @param throwable
     * @return
     */
    private static String findRootCause(final Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            throwable.printStackTrace(pw);
        } catch (RuntimeException ex) {
        }
        pw.flush();
        String result = sw.toString();
        if (result != null) {
            if (result.indexOf("Caused by") > -1) {
                result = result.substring(result.indexOf("Caused by"));
            }
        }
        if (result != null && result.length() > 2000) {
            result = result.substring(0, 2000);
        }
        return result;
    }


}
