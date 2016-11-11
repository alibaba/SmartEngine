package com.alibaba.smart.framework.engine.pvm.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.util.ParamChecker;
import lombok.Data;
import lombok.EqualsAndHashCode;


import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.impl.DefaultActivityTransitionSelectInvoker;
import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.invocation.message.impl.DefaultMessage;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehaviorProvider;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;

/**
 * @author 高海军 帝奇  2016.11.11   TODO 看下存在性
 * @author ettear 2016.04.13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultPvmActivity extends AbstractPvmActivity<Activity> implements PvmActivity {

    private final static List<Integer>  EXECUTE_EVENTS = new ArrayList<>();

    static {

        EXECUTE_EVENTS.add(PvmEventConstant.ACTIVITY_START.getCode());
        EXECUTE_EVENTS.add(PvmEventConstant.ACTIVITY_EXECUTE.getCode());
        EXECUTE_EVENTS.add(PvmEventConstant.ACTIVITY_END.getCode());
    }

    @Override
    public Message execute(ExecutionContext context) {
        Message activityExecuteMessage = new DefaultMessage();
        ExecutionInstance executionInstance = context.getCurrentExecution();
        ActivityInstance activityInstance = executionInstance.getActivity();
        Map<String,Object> request = context.getRequest();

        //TODO event 机制不能这么 hack 进去。。
        if (request.containsKey("event")) {
            dealEvent(context,activityInstance,activityExecuteMessage);
            return activityExecuteMessage;
        }




        //TODO
        AbstractActivityBehaviorProvider<?> activityBehaviorProvider =   (AbstractActivityBehaviorProvider<?>) this.getProvider();
          activityBehaviorProvider.execute(this,context);
//        if(null !=customInvoker){
//             customInvoker.invoke(context);
//         }

        //TODO 不同节点,应该有不同的行为, 每个节点还是需要自己的行为. 不能解决差异性问题.  优先级:高
//        TaskInstance taskInstance = activityInstance.getTask();


//        if (null != taskInstance && InstanceStatus.completed != taskInstance.getStatus()) {// 任务未完成，直接暂停
//            activityInstance.setStatus(InstanceStatus.suspended);
//            Message message = new DefaultMessage();
//            message.setSuspend(true);
//            return message;
//        }

        // 重置状态
//        executionInstance.setStatus(InstanceStatus.running);



//        dealStep(context, activityInstance, activityExecuteMessage);

        return activityExecuteMessage;
    }

    private void dealEvent(ExecutionContext context, ActivityInstance activityInstance, Message activityExecuteMessage) {
        Map<String,Object> request = context.getRequest();
        String event = (String) request.get("event");
        ParamChecker.notNull(event,"assignEvent is null !");
        Message invokerMessage = this.invokeActivity(event,context);

        if (invokerMessage.isFault()) {
            invokerMessage.setSuspend(true);
            activityExecuteMessage.setFault(true);
        }
        if (invokerMessage.isSuspend()) {
            activityExecuteMessage.setSuspend(true);
        }

    }

    @Override
    protected Invoker createDefaultInvoker(String event) {
        if (PvmEventConstant.ACTIVITY_TRANSITION_SELECT.name().equals(event)) {
            return new DefaultActivityTransitionSelectInvoker( this);
        } else {
            return super.createDefaultInvoker(event);
        }
    }

    private Message invokeActivity(String event, ExecutionContext context) {
        Message message = this.fireEvent(event, context);
        if (null == message) {
            message = new DefaultMessage();
        }
        return message;
    }

//    private void dealStep(ExecutionContext context, ActivityInstance activityInstance, Message activityExecuteMessage) {
//        // 恢复上次暂停时的执行器
//        String currentStep = activityInstance.getCurrentStep();
//        Iterator<Integer> executeEventIterator = EXECUTE_EVENTS.iterator();
//        if (StringUtils.isNotBlank(currentStep)) {
//            while (executeEventIterator.hasNext()) {
//                int  event = executeEventIterator.next();
//                if (event == Integer.valueOf(currentStep)) {
//                    break;
//                }
//            }
//        }
//        // 从上次暂停点开始执行
//        while (executeEventIterator.hasNext()) {
//            int event = executeEventIterator.next();
//            String eventName = PvmEventConstant.getName(event);
//            Message invokerMessage = this.invokeActivity(eventName, context);
//            if (invokerMessage.isFault()) {
//                invokerMessage.setSuspend(true);
//                activityExecuteMessage.setFault(true);
//            }
//            if (invokerMessage.isSuspend()) {
//                activityExecuteMessage.setSuspend(true);
//                break;
//            }
//        }
//    }

    @Override
    public String toString() {
        return " [id=" + getModel().getId() + "]";
    }



}
