package com.alibaba.smart.framework.engine.modules.smart.provider.process;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.smart.assembly.process.SmartTask;
import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class TaskProviderFactory implements
    ActivityProviderFactory<SmartTask> {

    private final static String DEFAULT_ACTION = PvmEventConstant.ACTIVITY_EXECUTE.name();

    private ExtensionPointRegistry extensionPointRegistry;

    public TaskProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;

    }

    @Override
    public TaskBehavior createActivityProvider(PvmActivity activity) {
        return new TaskBehavior(this.extensionPointRegistry, activity);
    }

    @Override
    public Class<SmartTask> getModelType() {
        return SmartTask.class;
    }

}
