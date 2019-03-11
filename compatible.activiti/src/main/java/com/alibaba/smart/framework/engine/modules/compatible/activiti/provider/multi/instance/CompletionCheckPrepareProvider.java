package com.alibaba.smart.framework.engine.modules.compatible.activiti.provider.multi.instance;

import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.MultiInstanceCounter;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.provider.Performer;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public class CompletionCheckPrepareProvider implements Performer {
    private ExtensionPointRegistry extensionPointRegistry;

    CompletionCheckPrepareProvider(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public Object perform(ExecutionContext context) {
        SmartEngine smartEngine = this.extensionPointRegistry.getExtensionPoint(SmartEngine.class);

        ActivityInstance activityInstance = context.getActivityInstance();
        MultiInstanceCounter multiInstanceCounter = context.getProcessEngineConfiguration().getMultiInstanceCounter();
        Long completedInstanceCount = multiInstanceCounter.countPassedTaskInstanceNumber(
            activityInstance.getProcessInstanceId(), activityInstance.getInstanceId(),

            smartEngine);

        Long rejectedInstanceCount = multiInstanceCounter.countRejectedTaskInstanceNumber(
            activityInstance.getProcessInstanceId(), activityInstance.getInstanceId(),

            smartEngine);

        Long instanceCount = null;
        if (null != activityInstance.getExecutionInstanceList()) {
            int size = activityInstance.getExecutionInstanceList().size();
            instanceCount = Long.valueOf(size);
        } else {
            instanceCount = 0L;
        }

        Map<String, Object> privateContext = context.getPrivateContext();
        // 此变量nrOfCompletedInstances命名并不合适,但是为了兼容 Activiti 不得不这样做.
        privateContext.put("nrOfCompletedInstances", completedInstanceCount);
        privateContext.put("nrOfRejectedInstance", rejectedInstanceCount);
        privateContext.put("nrOfInstances", instanceCount);

        return null;
    }
}
