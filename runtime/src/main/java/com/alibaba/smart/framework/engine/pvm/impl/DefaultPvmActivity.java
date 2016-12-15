package com.alibaba.smart.framework.engine.pvm.impl;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.common.util.ParamChecker;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultPvmActivity extends AbstractPvmActivity<Activity> implements PvmActivity {


    @Override
    public void execute(ExecutionContext context) {

        ActivityBehavior activityBehaviorProvider = this.getActivityBehavior();
        activityBehaviorProvider.execute(this, context);
    }

    private void dealEvent(ExecutionContext context, ActivityInstance activityInstance) {
        Map<String, Object> request = context.getRequest();
        String event = (String) request.get("event");
        ParamChecker.notNull(event, "assignEvent is null !");
        this.invokeActivity(event, context);


    }


    private void invokeActivity(String event, ExecutionContext context) {
        this.fireEvent(event, context);
    }


    @Override
    public String toString() {
        return " [id=" + getModel().getId() + "]";
    }


    // //TODO
    @Override
    public void fireEvent(String event, ExecutionContext context) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
