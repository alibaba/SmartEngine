package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.invocation.message.impl.DefaultMessage;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.action.Action;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ServiceTask;
import com.alibaba.smart.framework.engine.modules.bpmn.provider.task.action.SpringAction;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

public class ServiceTaskInvoker implements Invoker {

    private PvmActivity pvmActivity;

    public ServiceTaskInvoker(PvmActivity pvmActivity) {
        this.pvmActivity = pvmActivity;
    }

    @Override
    public Message invoke(ExecutionContext executionContext) {

        ServiceTask serviceTask = (ServiceTask) pvmActivity.getModel();
        Action action = serviceTask.getAction();
        if (action == null) {
            DefaultMessage defaultMessage = new DefaultMessage();
            return defaultMessage;
        }
        
        if (action.getType().equals("spring")) {
            SpringAction springAction = new SpringAction(action.getBean(),action.getMethod(),executionContext.getRequest());
            try {
                return springAction.execute();
            } catch (Throwable e) {
                //这里好像抓不到异常了已经
            }


        }

        return null;
    }
}
