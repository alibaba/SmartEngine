package com.alibaba.smart.framework.engine.pvm.impl;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.provider.TransitionBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public class DefaultPvmProcessInstance implements PvmProcessInstance {

    @Override
    public ProcessInstance start(ExecutionContext executionContext) {

        PvmProcessDefinition pvmProcessDefinition = executionContext.getPvmProcessDefinition();
        PvmActivity startActivity = pvmProcessDefinition.getStartActivity();

        startActivity.execute( executionContext);

        return executionContext.getProcessInstance();

    }




    @Override
    public ProcessInstance signal(PvmActivity pvmActivity, ExecutionContext executionContext) {

        pvmActivity.leave( executionContext);

        return executionContext.getProcessInstance();

    }

}
