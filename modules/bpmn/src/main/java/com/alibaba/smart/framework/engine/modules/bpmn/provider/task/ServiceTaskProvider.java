package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.message.impl.NotSubsbandMessage;
import com.alibaba.smart.framework.engine.invocation.message.impl.SubsbandMessage;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ServiceTask;
import com.alibaba.smart.framework.engine.modules.bpmn.provider.event.ProcessEventInvoker;
import com.alibaba.smart.framework.engine.modules.bpmn.provider.process.AbstractBpmnActivityProvider;
import com.alibaba.smart.framework.engine.provider.ActivityProvider;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

public class ServiceTaskProvider extends AbstractBpmnActivityProvider<ServiceTask> implements ActivityProvider<ServiceTask> {

    public ServiceTaskProvider(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }


    @Override
    public boolean containAction(String action) {
        ServiceTask serviceTask = (ServiceTask) this.getRuntimeActivity().getModel();
        return serviceTask.getAction().getId().equals(action);
    }

    @Override
    protected Invoker createExecuteInvoker() {

        return new ServiceTaskInvoker(getRuntimeActivity());
    }



    @Override
    public Invoker createCustomInvoker(PvmActivity runtimeActivity) {
       return new ServiceTaskInvoker(  runtimeActivity);
    }

    @Override
    protected Invoker createEventInvoker(String event) {
        return new  ProcessEventInvoker(getRuntimeActivity());
    }

    @Override
    protected Invoker createStartInvoker() {
        ServiceTask serviceTask = (ServiceTask) this.getRuntimeActivity().getModel();
        if (serviceTask.isAuto()) {
            return context -> new NotSubsbandMessage();
        }
        return context -> new SubsbandMessage();
    }
}
