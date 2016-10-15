package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;

import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;
import com.alibaba.smart.framework.engine.instance.util.ClassLoaderUtil;
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
        String className = serviceTask.getClassName();

        //TODO
        if(null == className){

            Action action = serviceTask.getAction();
            if (action == null) {
                DefaultMessage defaultMessage = new DefaultMessage();
                return defaultMessage;
            }

            if (action.getType().equals("spring")) {
                SpringAction springAction = new SpringAction(action.getId(),action.getMethod(),executionContext.getRequest());
                try {
                    return springAction.execute();
                } catch (Throwable e) {
                    //这里好像抓不到异常了已经
                }


            }
        }else{
            // TODO need cache,rename
            Object ss = ClassLoaderUtil.createNewInstance(className);
            if (ss instanceof TccDelegation<?>) {
                TccDelegation<?> tccDelegation = (TccDelegation<?>) ss;
                TccResult<?> tccResult = tccDelegation.tryExecute(executionContext);
                DefaultMessage defaultMessage = new DefaultMessage();
                defaultMessage.setBody(tccResult);
                return defaultMessage;
            }
        }





        return null;
    }
}
