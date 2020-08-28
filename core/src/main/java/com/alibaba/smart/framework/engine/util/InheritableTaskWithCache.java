package com.alibaba.smart.framework.engine.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author guoxing
 * @date 2020/8/23
 * @description 描述
 */
public abstract class InheritableTaskWithCache implements Callable<Void> {
    private Object obj;
    private static volatile Field inheritableThreadLocalsField;
    private static volatile Class threadLocalMapClazz;
    private static volatile Method createInheritedMapMethod;
    private static final Object accessLock = new Object();


    public InheritableTaskWithCache(){
        try{
            Thread currentThread = Thread.currentThread();
            Field field = getInheritableThreadLocalsField();
            // 得到当前线程中的inheritableThreadLocals熟悉值ThreadLocalMap, key是各种inheritableThreadLocal，value是值
            Object threadLocalMapObj = field.get(currentThread);
            if(threadLocalMapObj != null){
                Class threadLocalMapClazz = getThreadLocalMapClazz();
                Method method =  getCreateInheritedMapMethod(threadLocalMapClazz);
                // 创建一个新的ThreadLocalMap
                Object newThreadLocalMap = method.invoke(ThreadLocal.class,threadLocalMapObj);
                obj = newThreadLocalMap;
            }
        }catch (Exception e){
            throw new IllegalStateException(e);
        }
    }

    private Class getThreadLocalMapClazz(){
        if(inheritableThreadLocalsField == null){
            return null;
        }else {
            if(threadLocalMapClazz == null){
                synchronized (accessLock){
                    if(threadLocalMapClazz == null){
                        Class clazz = inheritableThreadLocalsField.getType();
                        threadLocalMapClazz = clazz;
                    }
                }
            }
        }
        return threadLocalMapClazz;
    }

    private Field getInheritableThreadLocalsField(){
        if(inheritableThreadLocalsField == null){
            synchronized (accessLock){
                if(inheritableThreadLocalsField == null){
                    try {
                        Field field = Thread.class.getDeclaredField("inheritableThreadLocals");
                        field.setAccessible(true);
                        inheritableThreadLocalsField = field;
                    }catch (Exception e){
                        throw new IllegalStateException(e);
                    }
                }
            }
        }
        return inheritableThreadLocalsField;
    }

    private Method getCreateInheritedMapMethod(Class threadLocalMapClazz){
        if(threadLocalMapClazz != null && createInheritedMapMethod == null){
            synchronized (accessLock){
                if(createInheritedMapMethod == null){
                    try {
                        Method method =  ThreadLocal.class.getDeclaredMethod("createInheritedMap",threadLocalMapClazz);
                        method.setAccessible(true);
                        createInheritedMapMethod = method;
                    }catch (Exception e){
                        throw new IllegalStateException(e);
                    }
                }
            }
        }
        return createInheritedMapMethod;
    }

    public abstract void runTask();

    @Override
    public Void call() throws Exception {
        boolean isSet = false;
        Thread currentThread = Thread.currentThread();
        Field field = getInheritableThreadLocalsField();
        try {
            if (obj != null && field != null) {
                field.set(currentThread, obj);
                obj = null;
                isSet = true;
            }
            // 执行任务
            runTask();
        }catch (Exception e){
            throw new IllegalStateException(e);
        }finally {
            // 最后将线程中的InheritableThreadLocals设置为null
            try{
                field.set(currentThread,null);
            }catch (Exception e){
                throw new IllegalStateException(e);
            }
        }
        return null;
    }
}
