package com.alibaba.smart.framework.engine.modules.bpmn.provider.event;

import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.EndEvent;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;

public class EndEventBehavior extends AbstractActivityBehavior<EndEvent> {

    public EndEventBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }

    @Override
    public void leave(ExecutionContext context) {
        ProcessInstance processInstance = context.getProcessInstance();
        processInstance.setStatus(InstanceStatus.completed);
        processInstance.setCompleteTime(DateUtil.getCurrentDate());
        Map<String, Object> request = context.getRequest();

        if(null != request){
            Object taskInstanceTag = request.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_TAG);
            //processInstance的tag使用的是最后一个完成的任务的tag
            if (taskInstanceTag != null) {
                processInstance.setTag(taskInstanceTag.toString());
            }
        }



        //子流程结束时,才会进入到该环节里面来。这个时候没要慌,需要找出父流程的执行实例id,然后继续执行父流程的后续节点。
        if(null !=  processInstance.getParentInstanceId()){
            Long parentExecutionInstanceId = processInstance.getParentExecutionInstanceId();


            //BE AWARE:这里重新驱动了父流程的流转,但是强烈依赖此时的线程上下文数据要变成父流程的。
            //BUT BUT BUT !!! 如果此时,子流程是全自动节点,但是父流程还没机会去完成实例化的话,下面的signal方法是拿不到数据的。

            //PersisterStrategy persisterStrategy = context.getProcessEngineConfiguration().getPersisterStrategy();
            //persisterStrategy.getProcessInstanceByExecutionInstanceId(parentExecutionInstanceId);

            SmartEngine smartEngine = context.getExtensionPointRegistry().getExtensionPoint(SmartEngine.class);
            ExecutionCommandService executionCommandService = smartEngine.getExecutionCommandService();
            executionCommandService.signal(parentExecutionInstanceId);
        }
    }
}
