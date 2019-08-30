package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ServiceTask;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtensionBinding(type = ExtensionConstant.ACTIVITY_BEHAVIOR,binding = ServiceTask.class)

public class ServiceTaskBehavior extends AbstractActivityBehavior<ServiceTask> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTaskBehavior.class);


    public ServiceTaskBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }

    public ServiceTaskBehavior() {
        super();
    }

    @Override
    public void execute(ExecutionContext context) {
        //tune
        super.execute(context);

        ServiceTask model = this.getModel();
        String className  =  model.getProperties().get("class");

        BehaviorUtil.behavior(context, className,this.extensionPointRegistry,this.getPvmActivity());

    }



}
