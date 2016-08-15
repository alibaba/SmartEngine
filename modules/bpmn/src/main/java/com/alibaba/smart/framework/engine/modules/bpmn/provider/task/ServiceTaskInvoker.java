package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;
import com.alibaba.smart.framework.engine.instance.util.ClassLoaderUtil;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.invocation.message.impl.DefaultMessage;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ServiceTask;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

public class ServiceTaskInvoker implements Invoker {

    private PvmActivity pvmActivity;

    public ServiceTaskInvoker(PvmActivity pvmActivity) {
        this.pvmActivity = pvmActivity;
    }

    @Override
    public Message invoke(ExecutionContext executionContext) {

        ServiceTask xx = (ServiceTask) pvmActivity.getModel();
        String className = xx.getClassName();
        
        if(null == className){
            throw new IllegalArgumentException("className should not be null");
        }

        // TODO need cache,rename
        Object ss = ClassLoaderUtil.createNewInstance(className);
        if (ss instanceof TccDelegation<?>) {
            TccDelegation<?> xxxx = (TccDelegation<?>) ss;
            TccResult<?> xx11 = xxxx.tryExecute(executionContext);
            DefaultMessage defaultMessage = new DefaultMessage();
            defaultMessage.setBody(xx11);
            return defaultMessage;
        }

        return null;
    }
}
