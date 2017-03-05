package com.alibaba.smart.framework.engine.pvm.impl;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.provider.TransitionBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.common.util.ParamChecker;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
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
        activityBehaviorProvider.enter(this, context);

        if (context.isNeedPause()) {
            // break;
            return;
        }

        leave( context);
    }


    @Override
    public  void leave( ExecutionContext context) {

        //执行每个节点的hook方法
        ActivityBehavior activityBehavior =  this.getActivityBehavior();
        activityBehavior.leave(this,context);

        Map<String, PvmTransition> outcomeTransitions = this.getOutcomeTransitions();

        if (null != outcomeTransitions && !outcomeTransitions.isEmpty()) {
            List<PvmTransition> matchedTransitions = new ArrayList<PvmTransition>(outcomeTransitions.size());
            for (Map.Entry<String, PvmTransition> transitionEntry : outcomeTransitions.entrySet()) {
                PvmTransition pendingTransition = transitionEntry.getValue();
                TransitionBehavior transitionBehavior = pendingTransition.getTransitionBehavior();
                boolean matched = transitionBehavior.execute(pendingTransition, context);

                if (matched) {
                    matchedTransitions.add(pendingTransition);
                }

            }
            //TODO 针对互斥和并行网关的线要检验,返回值只有一个或者多个。如果无则抛异常。

            for (PvmTransition matchedTransition : matchedTransitions) {
                PvmActivity targetPvmActivity = matchedTransition.getTarget();
                this.executeRecursively(targetPvmActivity, context);
            }


        }
    }

    private void executeRecursively(PvmActivity pvmActivity, ExecutionContext context) {

        //重要: 执行当前节点,会触发当前节点的行为执行
        pvmActivity.execute(context);


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
