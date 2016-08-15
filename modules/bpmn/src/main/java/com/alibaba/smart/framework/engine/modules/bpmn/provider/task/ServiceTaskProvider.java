package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;

import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.impl.DoNothingInvoker;
import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.invocation.message.impl.DefaultMessage;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ServiceTask;
import com.alibaba.smart.framework.engine.modules.bpmn.provider.process.AbstractBpmnActivityProvider;
import com.alibaba.smart.framework.engine.provider.ActivityProvider;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

public class ServiceTaskProvider extends AbstractBpmnActivityProvider<ServiceTask> implements ActivityProvider<ServiceTask> {

    public ServiceTaskProvider(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }

    private static int runtimes = 1;

    @Override
    protected Invoker createExecuteInvoker() {

        ServiceTask serviceTask = (ServiceTask) this.getRuntimeActivity().getModel();
        if (serviceTask.isAuto()) {
            return DoNothingInvoker.instance;
        }else {
            return context -> {
                DefaultMessage message = new DefaultMessage();
                if (serviceTask.getId().equals("theTask2")) {
                    if (runtimes == 2) {
                        System.out.printf("run right");
                        message.setSuspend(false);
                        message.setFault(false);
                        return message;

                    }
                  runtimes++;

                }
                message.setFault(true);
                message.setSuspend(true);
                return message;
            };
        }


    }
}
