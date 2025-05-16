package com.alibaba.smart.framework.engine.bpmn.behavior.callactivity;

import com.alibaba.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.callactivity.CallActivity;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
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

import java.util.HashMap;
import java.util.Map;

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

        Map<String, Object> subRequest = null;
        if(parentContext.getTenantId()!=null) {
            subRequest = new HashMap<>();
            subRequest.put(RequestMapSpecialKeyConstant.TENANT_ID, parentContext.getTenantId());
        }

        AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();

        //隔离父子流程的request和response,业务上如果有需要共享的,可以在第一个context里面手动从parentContext获取.
        ProcessInstance childProcessInstance = processInstanceFactory.createChild(processEngineConfiguration,   processDefinitionId,processDefinitionVersion,
                subRequest,  parentInstanceId,   parentExecutionInstanceId);

        ExecutionContext subContext = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON, ContextFactory.class)
            .createProcessContext( processEngineConfiguration , childProcessInstance,
                    subRequest,   null,parentContext);


        // TUNE 减少不必要的对象创建
        PvmProcessInstance pvmProcessInstance = new DefaultPvmProcessInstance();

        childProcessInstance = pvmProcessInstance.start(subContext);

        childProcessInstance = CommonServiceHelper.insertAndPersist(childProcessInstance, null, processEngineConfiguration);

        InstanceStatus childProcessInstanceStatus = childProcessInstance.getStatus();

        //如果子流程完成，则需要返回到父流程继续执行；否则，则暂停
        boolean childProcessInstanceCompleted = InstanceStatus.completed.equals(childProcessInstanceStatus);

        if(childProcessInstanceCompleted){
            return  false;
        }else {
            return true;
        }
    }

    //@Override
    //public void execute(ExecutionContext context, PvmActivity pvmActivity) {
    //
    //   throw new SignalException("cant be signal");
    //
    //}

}