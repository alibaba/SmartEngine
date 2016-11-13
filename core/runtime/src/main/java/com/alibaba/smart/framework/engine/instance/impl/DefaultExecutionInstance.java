package com.alibaba.smart.framework.engine.instance.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.DatabaseMod;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.param.ExecutionParam;
import com.google.common.base.Strings;

/**
 * DefaultExecutionInstance Created by ettear on 16-4-19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultExecutionInstance extends AbstractLifeCycleInstance implements ExecutionInstance,DatabaseMod<DefaultExecutionInstance,ExecutionParam> {

    private static final long serialVersionUID = 2323809298485587299L;
    private String            processInstanceId;
    // private InstanceFact fact;
    //TODO 去掉对象关联
    private String  activityId;
//    private boolean           needPause;



    @Override
    public String toDatabase() {


       return Strings.nullToEmpty(getInstanceId())
               + "|"
               +Strings.nullToEmpty(activityId)
               + "|";
//               +Strings.nullToEmpty(getActivity().getCurrentStep());

    }


    //TODO fix typo
    @Override
    public DefaultExecutionInstance getModel(ExecutionParam param) {

        this.setProcessInstanceId(param.getProcessId());
        this.setInstanceId(param.getExecutionId());
        return this;

    }


}
