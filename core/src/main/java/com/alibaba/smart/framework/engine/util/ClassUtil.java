package com.alibaba.smart.framework.engine.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.alibaba.smart.framework.engine.exception.EngineException;

import com.esotericsoftware.reflectasm.ConstructorAccess;

public abstract  class ClassUtil {



    private  static Map<Class,Object> objectMap = new ConcurrentHashMap<Class, Object>();

    private  static Map<String,Class> classMap = new ConcurrentHashMap<String, Class>();

    private static Lock lock = new ReentrantLock();

    public static ClassLoader getContextClassLoader() {
        return  Thread.currentThread().getContextClassLoader();
    }

    public static ClassLoader getFallbackClassLoader() {
        return ClassUtil.class.getClassLoader();
    }

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

            clazz = classMap.get(className);

            if(null == clazz){
                clazz = getContextClassLoader().loadClass(className);
                classMap.put(className,clazz);
            }

        } catch (ClassNotFoundException e) {
            throw new EngineException(e);
        }

        return createOrGetInstance(clazz);



    }


    public static Constructor getConstruct(Class clazz, Class... argTypes) throws EngineException {
        String className = clazz.getName();

        try {
            Constructor constructor = clazz.getConstructor(argTypes);
            return constructor;
        } catch (NoSuchMethodException e) {
            throw new EngineException("Unable to load class " + className + ". Initial cause was " + e.getMessage(), e);
        } catch (SecurityException e) {
            throw new EngineException("Unable to load class " + className + ". Initial cause was " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new EngineException("Unable to load class " + className + ". Initial cause was " + e.getMessage(), e);
        }

    }


    public static Object createNewInstance(Constructor constructor, Object... args) throws EngineException {
        String className =  constructor.getClass().getName();

        try {
            Object newInstance = constructor.newInstance(args);
            return newInstance;

        } catch (IllegalAccessException e) {
            throw new EngineException("Unable to load class " + className + ". Initial cause was " + e.getMessage(), e);
        } catch (InstantiationException e) {
            throw new EngineException("Unable to load class " + className + ". Initial cause was " + e.getMessage(), e);
        } catch (SecurityException e) {
            throw new EngineException("Unable to load class " + className + ". Initial cause was " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new EngineException("Unable to load class " + className + ". Initial cause was " + e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new EngineException("Unable to load class " + className + ". Initial cause was "
                + e.getCause().getMessage(), e.getCause());
        }
    }



    public static Class loadClass(String className)  {
        Class clazz;

        try {
            clazz = Class.forName(className, true, getContextClassLoader());
        } catch (ClassNotFoundException e) {
            //   fallback
            try {
                clazz = Class.forName(className, true, getFallbackClassLoader());
            } catch (ClassNotFoundException e1) {
                throw new EngineException("Unable to load class " + className + ". Initial cause was "
                    + e.getCause().getMessage(), e.getCause());
            }
        }

        return clazz;
    }



}
