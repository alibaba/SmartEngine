package com.alibaba.smart.framework.engine.test;

import com.alibaba.smart.framework.engine.configuration.LockStrategy;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.LockException;
import com.alibaba.smart.framework.engine.model.assembly.IdBasedElement;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;

import org.springframework.stereotype.Service;

/**
 * Created by 高海军 帝奇 74394 on 2017 January  18:03.
 */
@Service
public class DoNothingLockStrategy implements LockStrategy {


    @Override
    public void tryLock(String processInstanceId, ExecutionContext context) throws LockException {
        //ExtensionPointRegistry extensionPointRegistry = smartEngine.getProcessEngineConfiguration()
        //    .getExtensionPointRegistry();
        //PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        //
        //ProcessInstanceStorage processInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(ProcessInstanceStorage.class);
        //
        //processInstanceStorage.update()

        //ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("processInstanceDAO");
        //
        //processInstanceDAO.tryLock(Long.valueOf(processInstanceId));

        //可以是设置 db uniqueKey 唯一索引； 或者在插入后直接再锁上； 或者使用其他中间件。

        if(context != null){
            String processDefinitionActivityId = context.getExecutionInstance().getProcessDefinitionActivityId();

            ProcessDefinition processDefinition = context.getProcessDefinition();

            IdBasedElement idBasedElement = processDefinition.getIdBasedElementMap().get(processDefinitionActivityId);
            idBasedElement.getId();

        }




    }

    @Override
    public void unLock(String processInstanceId, ExecutionContext context) throws LockException {
        //do nothing
    }


}
