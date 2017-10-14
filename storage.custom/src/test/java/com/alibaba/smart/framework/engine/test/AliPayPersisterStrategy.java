package com.alibaba.smart.framework.engine.test;//package com.alibaba.smart.framework.engine.persister.util;

import com.alibaba.smart.framework.engine.configuration.PersisterStrategy;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.util.InstanceSerializerFacade;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  11:17.
 */
public class AliPayPersisterStrategy implements PersisterStrategy {
    private static Map<Serializable, String> processInstanceMap = new ConcurrentHashMap<Serializable, String>();

    //private static Map<Serializable, String> executionIdProcessInstanceMap = new ConcurrentHashMap<Serializable, String>();


    @Override
    public String insert(ProcessInstance processInstance) {

        String str =  InstanceSerializerFacade.serialize(processInstance);
        processInstanceMap.put(processInstance.getInstanceId(),str);
        return str;

    }


    @Override
    public String update(ProcessInstance processInstance) {

        String str =  InstanceSerializerFacade.serialize(processInstance);
        processInstanceMap.put(processInstance.getInstanceId(),str);
        return str;

    }

    @Override
    public ProcessInstance getProcessInstance(Long processInstanceId){

        String str = processInstanceMap.get(processInstanceId);
        return InstanceSerializerFacade.deserializeAll(str);

    }

    @Override
    public ProcessInstance getProcessInstanceByExecutionInstanceId(Long executionInstanceId){
        // 应该考虑存储下来eid 和pid的映射关系,这样就不用再特意查找了。
        // 本次示例里,通过遍历processInstanceMap来获取,实际中并不可取。

        ProcessInstance machedProcessInstance = null;
        boolean matched= false;


        for (String str : processInstanceMap.values()) {

            ProcessInstance processInstance= InstanceSerializerFacade.deserializeAll(str);

            List<ActivityInstance> activityInstances =  processInstance.getActivityInstances();


            if(null == activityInstances ||activityInstances.isEmpty() ){

                continue;
            }else{
                int size = activityInstances.size();

                //TODO DUPLICATED CODE

                for (int i = size-1; i>=0;i--) {
                    ActivityInstance activityInstance = activityInstances.get(i);

                    List<ExecutionInstance> executionInstances =    activityInstance.getExecutionInstanceList();
                    for (ExecutionInstance tempExecutionInstance : executionInstances) {
                        if(null!= tempExecutionInstance && tempExecutionInstance.getInstanceId().equals(executionInstanceId)){
                            machedProcessInstance = processInstance;
                            matched = true;
                            break;

                        }
                    }


                }

            }




        }

        if(!matched){
            throw new EngineException("No processInstance found for executionInstanceId : "+executionInstanceId);
        }

        PersisterSession.currentSession().putProcessInstance(machedProcessInstance);

        return machedProcessInstance;

    }



}
