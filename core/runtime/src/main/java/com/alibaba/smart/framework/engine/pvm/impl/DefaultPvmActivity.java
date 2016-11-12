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
    public void execute(ExecutionContext context) {

        //TODO 居然强转
        AbstractActivityBehaviorProvider<?> activityBehaviorProvider =   (AbstractActivityBehaviorProvider<?>) this.getProvider();
        activityBehaviorProvider.execute(this,context);
    }

    private void dealEvent(ExecutionContext context, ActivityInstance activityInstance, Message activityExecuteMessage) {
        Map<String,Object> request = context.getRequest();
        String event = (String) request.get("event");
        ParamChecker.notNull(event,"assignEvent is null !");
        this.invokeActivity(event,context);


    }

    @Override
    protected Invoker createDefaultInvoker(String event) {
        if (PvmEventConstant.ACTIVITY_TRANSITION_SELECT.name().equals(event)) {
            return new DefaultActivityTransitionSelectInvoker( this);
        } else {
            return super.createDefaultInvoker(event);
        }
    }

    private void invokeActivity(String event, ExecutionContext context) {
         this.fireEvent(event, context);
    }


    @Override
    public String toString() {
        return " [id=" + getModel().getId() + "]";
    }



}
