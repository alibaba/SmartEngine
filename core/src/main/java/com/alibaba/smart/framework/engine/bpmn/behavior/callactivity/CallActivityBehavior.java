package com.alibaba.smart.framework.engine.bpmn.behavior.callactivity;

import com.alibaba.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.callactivity.CallActivity;
import com.alibaba.smart.framework.engine.bpmn.assembly.multi.instance.MultiInstanceLoopCharacteristics;
import com.alibaba.smart.framework.engine.bpmn.assembly.task.UserTask;
import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.factory.ContextFactory;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.exception.SignalException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessInstance;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmProcessInstance;
import com.alibaba.smart.framework.engine.service.command.impl.CommonServiceHelper;

import com.sun.xml.internal.ws.api.pipe.Engine;

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
        boolean call = this.call(processInstance.getInstanceId(), executionInstance.getInstanceId(),
            callActivity, context);
        return call;
    }

    //FIXME ettear 与DefaultProcessCommandService的逻辑合并
    private boolean call(String parentInstanceId, String parentExecutionInstanceId,CallActivity callActivity, ExecutionContext context) {

        String processDefinitionId =  callActivity.getCalledElement();
        String version = callActivity.getCalledElementVersion();

        AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();

        ExecutionContext subContext = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON, ContextFactory.class)
            .create();
        subContext.setParent(context);

        subContext.setProcessEngineConfiguration(processEngineConfiguration);
        subContext.setRequest(context.getRequest());

        ProcessDefinition pvmProcessDefinition = annotationScanner.getExtensionPoint(ExtensionConstant.SERVICE,
            ProcessDefinitionContainer.class).getProcessDefinition(processDefinitionId, version);
        subContext.setProcessDefinition(pvmProcessDefinition);




        // TUNE 减少不必要的对象创建
        PvmProcessInstance pvmProcessInstance = new DefaultPvmProcessInstance();

        ProcessInstance subProcessInstance = processInstanceFactory.create(subContext);
        subProcessInstance.setParentInstanceId(parentInstanceId);
        subProcessInstance.setParentExecutionInstanceId(parentExecutionInstanceId);

        subContext.setProcessInstance(subProcessInstance);

        subProcessInstance = pvmProcessInstance.start(subContext);

        subProcessInstance = CommonServiceHelper.insertAndPersist(subProcessInstance, context.getRequest(), processEngineConfiguration);

        return InstanceStatus.completed!=subProcessInstance.getStatus();
    }

    //@Override
    //public void execute(ExecutionContext context, PvmActivity pvmActivity) {
    //
    //   throw new SignalException("cant be signal");
    //
    //}

}