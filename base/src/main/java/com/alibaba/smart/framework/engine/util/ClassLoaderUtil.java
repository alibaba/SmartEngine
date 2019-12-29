package com.alibaba.smart.framework.engine.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.alibaba.smart.framework.engine.exception.EngineException;

import com.esotericsoftware.reflectasm.ConstructorAccess;

public abstract  class ClassLoaderUtil {



    private  static Map<Class,Object> objectMap = new ConcurrentHashMap<Class, Object>();
    private static Lock lock = new ReentrantLock();

    public static ClassLoader getContextClassLoader() {
        return  Thread.currentThread().getContextClassLoader();
    }



    public static ClassLoader getFallbackClassLoader() {
        return ClassLoaderUtil.class.getClassLoader();
    }

    //FIXME
    public static Object createNewInstance(Class clazz) {

            ConstructorAccess access = ConstructorAccess.get(clazz);
            return access.newInstance();
    }


    public static Object createOrGetInstance(Class clazz) {

            Object object = objectMap.get(clazz);

            if(object == null){
                ConstructorAccess access = ConstructorAccess.get(clazz);
                object  = access.newInstance();
                objectMap.put(clazz,object);
                return object;
            }else{
                return object;
            }

    }

    public static Object createOrGetInstance(String className) throws EngineException {
        Class clazz;
        try {
            clazz = getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new EngineException(e);
        }

        return createOrGetInstance(clazz);



    }


    public static Object createOrGetInstance(String className, Class[] argTypes, Object[] args) throws EngineException {
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


    public static Class loadClass(String className) throws ClassNotFoundException {
        Class clazz;
        try {
            clazz = Class.forName(className, true, getContextClassLoader());
        } catch (ClassNotFoundException e) {
            //   fallback
            clazz = Class.forName(className, true, getFallbackClassLoader());
        }

        return clazz;
    }



}
