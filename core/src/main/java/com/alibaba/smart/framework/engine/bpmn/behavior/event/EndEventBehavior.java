package com.alibaba.smart.framework.engine.bpmn.behavior.event;

import java.util.Map;

import com.alibaba.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.event.EndEvent;
import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.event.EventConstant;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;

@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = EndEvent.class)
public class EndEventBehavior extends AbstractActivityBehavior<EndEvent> {

    public EndEventBehavior() {
        super();
    }

    @Override
    public void leave(ExecutionContext context, PvmActivity pvmActivity) {
        fireEvent(context,pvmActivity, EventConstant.ACTIVITY_END);

        ProcessInstance processInstance = context.getProcessInstance();
        processInstance.setStatus(InstanceStatus.completed);
        processInstance.setCompleteTime(DateUtil.getCurrentDate());
        Map<String, Object> request = context.getRequest();

        if(null != request){
            Object taskInstanceTag = request.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_TAG);
            //非标准特性 HARDCODE： processInstance的tag使用的是最后一个完成的任务的tag
            if (taskInstanceTag != null) {
                processInstance.setTag(taskInstanceTag.toString());
            }
        }

        //==== 子流程结束，执行父流程 ====
        //子流程结束时,才会进入到该环节里面来。需要找出父流程的执行实例id,然后继续执行父流程的后续节点。
        String parentExecutionInstanceId = processInstance.getParentExecutionInstanceId();
        if (null != parentExecutionInstanceId) {
            //如果上下文中有父上下文，说明父子在同一线程中，父线程本身就会执行，所以不需要启动
            if (null == context.getParent()) {
                ExecutionCommandService executionCommandService =context.getProcessEngineConfiguration().getSmartEngine().getExecutionCommandService();
                executionCommandService.signal(parentExecutionInstanceId,context.getRequest());
            }
        }

        fireEvent(context, pvmActivity, EventConstant.PROCESS_END);
    }
}
