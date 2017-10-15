package com.alibaba.smart.framework.engine.modules.compatible.activiti.provider.multi.instance;

import java.util.Map;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.provider.Performer;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public class CompletionCheckProvider implements Performer {
    private ExtensionPointRegistry extensionPointRegistry;

    CompletionCheckProvider(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public Object perform(ExecutionContext context) {
        Map<String, Object> privateContext = context.getPrivateContext();
        Integer instanceCount=(Integer)privateContext.get("nrOfInstances");
        Integer completedInstanceCount=(Integer)privateContext.get("nrOfCompletedInstances");

        if(null!=instanceCount && null!=completedInstanceCount){
            return instanceCount.equals(completedInstanceCount);
        }
        return false;
    }
}
