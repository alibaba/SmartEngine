package com.alibaba.smart.framework.engine.common.util;

import java.util.Date;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;

/**
 * Created by 高海军 帝奇 74394 on 2017 June  10:35.
 */
public class MarkDoneUtil {

    public   static  void markDone(ActivityInstance activityInstance, ExecutionInstance executionInstance,ExtensionPointRegistry extensionPointRegistry) {
        Date completeDate = DateUtil.getCurrentDate();
        executionInstance.setCompleteDate(completeDate);
        executionInstance.setActive(false);
        if(null != activityInstance){
            activityInstance.setCompleteDate(completeDate);
            //TODO
            //activityInstance.setActive(false);
        }

        //TODO 这里可以把需要更新的对象放到另外一个队列中去,后面再统一更新。 需要结合 CustomExecutionInstanceStorage#Update 一起看下。 ettear
        /*
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        ExecutionInstanceStorage executionInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(ExecutionInstanceStorage.class);

        executionInstanceStorage.update(executionInstance);
        */

    }

}
