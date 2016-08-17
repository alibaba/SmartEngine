package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.UserTask;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

public class UserTaskInvoker implements Invoker {

    private PvmActivity pvmActivity;

    public UserTaskInvoker(PvmActivity pvmActivity) {
        this.pvmActivity = pvmActivity;
    }

    @Override
    public Message invoke(ExecutionContext executionContext) {

        @SuppressWarnings("unused")
        UserTask xx = (UserTask) pvmActivity.getModel();
//        String className = xx.getClassName();
//        
//        if(null == className){
//            throw new IllegalArgumentException("className should not be null");
//        }
//
//        // TODO need cache,rename
//        Object ss = ClassLoaderUtil.createNewInstance(className);
//        if (ss instanceof TccDelegation<?>) {
//            TccDelegation<?> xxxx = (TccDelegation<?>) ss;
//            TccResult<?> xx11 = xxxx.tryExecute(executionContext);
//            DefaultMessage defaultMessage = new DefaultMessage();
//            defaultMessage.setBody(xx11);
//            return defaultMessage;
//        }

        return null;
    }
}
