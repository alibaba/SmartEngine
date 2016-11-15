package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.util.ClassLoaderUtil;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.action.Action;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ServiceTask;
import com.alibaba.smart.framework.engine.modules.bpmn.provider.process.AbstractBpmnActivityBehaviorProvider;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

public class ServiceTaskBehaviorProvider extends AbstractBpmnActivityBehaviorProvider<ServiceTask> implements ActivityBehavior<ServiceTask> {

    public ServiceTaskBehaviorProvider(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }


    @Override
    public void execute(PvmActivity pvmActivity, ExecutionContext executionContext) {
        ProcessInstance processInstance = executionContext.getProcessInstance();
        ActivityInstance activityInstance = super.activityInstanceFactory.create(pvmActivity, processInstance);

        processInstance.addActivityInstance(activityInstance);


        ServiceTask serviceTask = (ServiceTask) pvmActivity.getModel();
        String className = serviceTask.getClassName();

        //TODO
        if (null == className) {

            Action action = serviceTask.getAction();
            if (action == null) {
                return;
            }

            if (action.getType().equals("spring")) {
//                SpringAction springAction = new SpringAction(action.getId(),action.getMethod(),executionContext.getRequest());
//                try {
//                    // TODO 考虑下是否需要给对应的返回值,和 wufeng 确认
//                     springAction.execute();
//                } catch (Throwable e) {
//                    //这里好像抓不到异常了已经
//                }


            }
        } else {
            // TODO need cache,rename
            Object ss = ClassLoaderUtil.createNewInstance(className);
            if (ss instanceof TccDelegation<?>) {
                TccDelegation<?> tccDelegation = (TccDelegation<?>) ss;
                TccResult<?> tccResult = tccDelegation.tryExecute(executionContext);
//                DefaultMessage defaultMessage = new DefaultMessage();
//                defaultMessage.setBody(tccResult);
//                return defaultMessage;
            }
        }


    }
}
