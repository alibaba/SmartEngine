package com.alibaba.smart.framework.engine.persister.util;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * Created by 高海军 帝奇 74394 on 2017 May  12:12.
 */
public class InstanceSerializerFacade {

    public static String serialize(ProcessInstance processInstance) {
       return InstanceSerializerV1.serialize(processInstance);
    }


    public static ProcessInstance deserializeAll(String serializeString){
        if(serializeString.startsWith("v0")){
            return InstanceSerializer.deserializeAll(serializeString);
        }else if(serializeString.startsWith("v1")){
            return InstanceSerializerV1.deserializeAll(serializeString);
        }else{
            throw new EngineException("unsupport version for deserializeAll:"+serializeString);
        }
    }
}
