package com.alibaba.smart.framework.engine.test;//package com.alibaba.smart.framework.engine.persister.alipay;

import com.alibaba.smart.framework.engine.common.persister.PersisterStrategy;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  11:17.
 */
public class AliPayPersisterStrategy implements PersisterStrategy {
    private static Map<Serializable, String> instances = new ConcurrentHashMap<Serializable, String>();

    public    void insert(Serializable id , String value) {
        instances.put(id,value);
    }

    public   void update(Serializable businessInstanceId ,String value) {
        instances.put(businessInstanceId,value);
    }

    public   String find(Serializable businessInstanceId) {
        return instances.get(businessInstanceId);
    }

    public   void remove(Serializable businessInstanceId) {
         instances.remove(businessInstanceId);
    }


}
