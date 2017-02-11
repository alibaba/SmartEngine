package com.alibaba.smart.framework.engine.modules.bpmn;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  15:31.
 */
public class MockedDBService {
    private static Map<Serializable, String> instances = new ConcurrentHashMap<Serializable, String>();

    public static  void insert(Serializable id , String value) {
        instances.put(id,value);
    }

    public static void update(Serializable businessInstanceId ,String value) {
        instances.put(businessInstanceId,value);
    }

    public static String find(Serializable businessInstanceId) {
        return instances.get(businessInstanceId);
    }

    public static void remove(Serializable businessInstanceId) {
         instances.remove(businessInstanceId);
    }
}
