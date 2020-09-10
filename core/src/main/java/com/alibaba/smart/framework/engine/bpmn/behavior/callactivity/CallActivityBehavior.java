package com.alibaba.smart.framework.engine.bpmn.behavior.callactivity;

import java.util.Map;

import com.alibaba.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.callactivity.CallActivity;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.factory.ContextFactory;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessInstance;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmProcessInstance;
import com.alibaba.smart.framework.engine.service.command.impl.CommonServiceHelper;

/**
 * Created by 高海军 帝奇 74394 on 2017 May  16:07.
 */
@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = CallActivity.class)
public class CallActivityBehavior extends AbstractActivityBehavior<CallActivity> {

    public CallActivityBehavior() {
        super();
    }

    @Override
    public boolean enter(ExecutionContext context, PvmActivity pvmActivity) {

        super.enter(context, pvmActivity);


        CallActivity callActivity = (CallActivity)pvmActivity.getModel();

        ProcessInstance processInstance = context.getProcessInstance();
        ExecutionInstance executionInstance = context.getExecutionInstance();

        boolean needPause = this.startChildProcessInstance(processInstance.getInstanceId(), executionInstance.getInstanceId(),
            callActivity, context);

        return needPause;
    }

    private boolean startChildProcessInstance(String parentInstanceId, String parentExecutionInstanceId, CallActivity callActivity, ExecutionContext parentContext) {

        String processDefinitionId =  callActivity.getCalledElement();
        String processDefinitionVersion = callActivity.getCalledElementVersion();


        Map<String, Object> request = parentContext.getRequest();
        Map<String, Object> response = parentContext.getResponse();


        AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();

        ProcessInstance childProcessInstance = processInstanceFactory.createChild(processEngineConfiguration,   processDefinitionId,processDefinitionVersion,
            request,  parentInstanceId,   parentExecutionInstanceId);


        ExecutionContext subContext = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON, ContextFactory.class)
            .create( processEngineConfiguration , childProcessInstance,
                  request,   response,parentContext);


        // TUNE 减少不必要的对象创建
        PvmProcessInstance pvmProcessInstance = new DefaultPvmProcessInstance();

        childProcessInstance = pvmProcessInstance.start(subContext);

        childProcessInstance = CommonServiceHelper.insertAndPersist(childProcessInstance, request, processEngineConfiguration);

        return InstanceStatus.completed != childProcessInstance.getStatus();
    }

    //@Override
    //public void execute(ExecutionContext context, PvmActivity pvmActivity) {
    //
    //   throw new SignalException("cant be signal");
    //
    //}

}