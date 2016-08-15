package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.DatabaseMod;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.param.ExecutionParam;
import com.google.common.base.Strings;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * DefaultExecutionInstance Created by ettear on 16-4-19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultExecutionInstance extends AbstractLifeCycleInstance implements ExecutionInstance,DatabaseMod<DefaultExecutionInstance,ExecutionParam> {

    private static final long serialVersionUID = 2323809298485587299L;
    private String            processInstanceId;
    // private InstanceFact fact;
    private ActivityInstance  activity;
    private boolean           fault;



    @Override
    public String toDatabase() {



       return Strings.nullToEmpty(getInstanceId())
               + "|"
               +Strings.nullToEmpty(getActivity().getActivityId())
               + "|"
               +Strings.nullToEmpty(getActivity().getCurrentStep());

    }

    @Override
    public DefaultExecutionInstance getModle(ExecutionParam param) {

        this.setProcessInstanceId(param.getProcessId());
        this.setInstanceId(param.getExecutionId());
        return this;

    }


}
