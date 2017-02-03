package com.alibaba.smart.framework.engine.persister.alipay;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  15:31.
 */
public class MockedDBService {
    private static Map<Long, String> instances = new ConcurrentHashMap<Long, String>();

    public static  void insert(Long orderId ,String value) {
        instances.put(orderId,value);
    }

    public static void update(Long orderId ,String value) {
        instances.put(orderId,value);
    }

    public static String find(Long orderId) {
        return instances.get(orderId);
    }

    public static void remove(Long orderId) {
         instances.remove(orderId);
    }
}
