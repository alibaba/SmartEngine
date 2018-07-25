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
        MultiInstanceCounter multiInstanceCounter = this.extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration().getMultiInstanceCounter();
        Integer completedInstanceCount = multiInstanceCounter.countPassedTaskInstanceNumber(
            activityInstance.getProcessInstanceId(), activityInstance.getInstanceId(),

            smartEngine);

        Integer rejectedInstanceCount = multiInstanceCounter.countRejectedTaskInstanceNumber(
            activityInstance.getProcessInstanceId(), activityInstance.getInstanceId(),

            smartEngine);

        Integer instanceCount;
        if (null != activityInstance.getExecutionInstanceList()) {
            instanceCount = activityInstance.getExecutionInstanceList().size();
        } else {
            instanceCount = 0;
        }

        Map<String, Object> privateContext = context.getPrivateContext();
        // 此变量nrOfCompletedInstances命名并不合适,但是为了兼容 Activiti 不得不这样做.
        privateContext.put("nrOfCompletedInstances", completedInstanceCount);
        privateContext.put("nrOfRejectedInstance", rejectedInstanceCount);
        privateContext.put("nrOfInstances", instanceCount);

        return null;
    }
}
