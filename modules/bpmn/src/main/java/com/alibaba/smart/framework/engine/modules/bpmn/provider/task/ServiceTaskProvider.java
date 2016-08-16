package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.impl.DoNothingInvoker;
import com.alibaba.smart.framework.engine.invocation.message.impl.DefaultMessage;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ServiceTask;
import com.alibaba.smart.framework.engine.modules.bpmn.provider.process.AbstractBpmnActivityProvider;
import com.alibaba.smart.framework.engine.provider.ActivityProvider;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

public class ServiceTaskProvider extends AbstractBpmnActivityProvider<ServiceTask> implements ActivityProvider<ServiceTask> {

    public ServiceTaskProvider(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }



    @Override
    protected Invoker createExecuteInvoker() {

        ServiceTask serviceTask = (ServiceTask) this.getRuntimeActivity().getModel();
        if (serviceTask.isAuto()) {
            return DoNothingInvoker.instance;
        } else {
            return context -> {
                DefaultMessage message = new DefaultMessage();
                if (serviceTask.getId().equals("theTask2")) {

                }
                message.setFault(true);
                message.setSuspend(true);
                return message;
            };
        }
    }



//    @Override
//    public Invoker createCustomInvoker(PvmActivity runtimeActivity) {
//       return new ServiceTaskInvoker(  runtimeActivity);
//    }
}
