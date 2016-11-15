package com.alibaba.smart.framework.engine.instance.util;

import com.alibaba.smart.framework.engine.exception.EngineException;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

/**
 * Keeps all classloading in ehcache consistent.
 *
 * @author Greg Luck
 * @version $Id: ClassLoaderUtil.java 1214 2009-09-25 00:14:13Z gbevin $ COPYIED FROM Ehcache
 */
@SuppressWarnings("rawtypes")
public final class ClassLoaderUtil {

    /**
     * Utility class.
     */
    private ClassLoaderUtil() {
        // noop
    }

    /**
     * Gets the <code>ClassLoader</code> that all classes in ehcache, and extensions, should use for classloading. All
     * ClassLoading in ehcache should use this one. This is the only thing that seems to work for all of the class
     * loading situations found in the wild.
     *
     * @return the thread context class loader.
     */
    public static ClassLoader getStandardClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * Gets a fallback <code>ClassLoader</code> that all classes in ehcache, and extensions, should use for
     * classloading. This is used if the context class loader does not work.
     *
     * @return the <code>ClassLoaderUtil.class.getClassLoader();</code>
     */
    public static ClassLoader getFallbackClassLoader() {
        return ClassLoaderUtil.class.getClassLoader();
    }

    /**
     * Creates a new class instance. Logs errors along the way. Classes are loaded using the ehcache standard
     * classloader.
     *
     * @param className a fully qualified class name
     * @return the newly created instance
     * @throws EngineException if instance cannot be created due to a missing class or exception
     */
    public static Object createNewInstance(String className) throws EngineException {
        return createNewInstance(className, new Class[0], new Object[0]);
    }

    /**
     * Creates a new class instance and passes args to the constructor call. Logs errors along the way. Classes are
     * loaded using the ehcache standard classloader.
     *
     * @param className a fully qualified class name
     * @param argTypes  Types for constructor argument parameters
     * @param args      Values for constructor argument parameters
     * @return the newly created instance
     * @throws EngineException if instance cannot be created due to a missing class or exception
     */
    @SuppressWarnings("unchecked")
    public static Object createNewInstance(String className, Class[] argTypes, Object[] args) throws EngineException {
        Class clazz;
        Object newInstance;
        try {
            clazz = loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new EngineException("Unable to load class " + className + ". Initial cause was " + e.getMessage(), e);
        }

        try {
            Constructor constructor = clazz.getConstructor(argTypes);
            newInstance = constructor.newInstance(args);
        } catch (IllegalAccessException e) {
            throw new EngineException("Unable to load class " + className + ". Initial cause was " + e.getMessage(), e);
        } catch (InstantiationException e) {
            throw new EngineException("Unable to load class " + className + ". Initial cause was " + e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            throw new EngineException("Unable to load class " + className + ". Initial cause was " + e.getMessage(), e);
        } catch (SecurityException e) {
            throw new EngineException("Unable to load class " + className + ". Initial cause was " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new EngineException("Unable to load class " + className + ". Initial cause was " + e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new EngineException("Unable to load class " + className + ". Initial cause was "
                    + e.getCause().getMessage(), e.getCause());
        }
        return newInstance;
    }

    /**
     * Load the given class by name
     *
     * @param className a fully qualified class name
     * @return Class the loaded class
     * @throws ClassNotFoundException if the class cannot be loaded
     * @since 1.7
     */
    public static Class loadClass(String className) throws ClassNotFoundException {
        Class clazz;
        try {
            clazz = Class.forName(className, true, getStandardClassLoader());
        } catch (ClassNotFoundException e) {
            // try fallback
            clazz = Class.forName(className, true, getFallbackClassLoader());
        }

        return clazz;
    }


    public static URL[] getResources(String resourceName) {
        LinkedList urls = new LinkedList();
        boolean found = false;

        // 首先试着从当前线程的ClassLoader中查找。
        found = getResources(urls, resourceName, getContextClassLoader(), false);

        // 如果没找到，试着从装入自己的ClassLoader中查找。
        if (!found) {
            getResources(urls, resourceName, ClassLoaderUtil.class.getClassLoader(), false);
        }

        // 最后的尝试: 在系统ClassLoader中查找(JDK1.2以上)，
        // 或者在JDK的内部ClassLoader中查找(JDK1.2以下)。
        if (!found) {
            getResources(urls, resourceName, null, true);
        }

        // 返回不重复的列表。
        return getDistinctURLs(urls);
    }


    public static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }


    private static URL[] getDistinctURLs(LinkedList urls) {
        if ((urls == null) || (urls.size() == 0)) {
            return new URL[0];
        }

        Set urlSet = new HashSet(urls.size());

        for (Iterator i = urls.iterator(); i.hasNext(); ) {
            URL url = (URL) i.next();

            if (urlSet.contains(url)) {
                i.remove();
            } else {
                urlSet.add(url);
            }
        }

        return (URL[]) urls.toArray(new URL[urls.size()]);
    }


    private static boolean getResources(List urlSet, String resourceName, ClassLoader classLoader,
                                        boolean sysClassLoader) {
        if (resourceName == null) {
            return false;
        }

        Enumeration i = null;

        try {
            if (classLoader != null) {
                i = classLoader.getResources(resourceName);
            } else if (sysClassLoader) {
                i = ClassLoader.getSystemResources(resourceName);
            }
        } catch (IOException e) {
        }

        if ((i != null) && i.hasMoreElements()) {
            while (i.hasMoreElements()) {
                urlSet.add(i.nextElement());
            }

            return true;
        }

        return false;
    }

}
