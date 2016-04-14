package com.alibaba.smart.framework.flow.model.bpmn.sequenceflow;

import com.alibaba.smart.framework.flow.model.bpmn.BaseProcessNode;
import com.alibaba.smart.framework.flow.model.bpmn.ProcessActivity;
import com.alibaba.smart.framework.flow.model.bpmn.gateway.RestrictionExpression;

public class ProcessSequenceFlow extends BaseProcessNode {

    private ProcessActivity       sourceActivity;
    private ProcessActivity       targetActivity;
    private RestrictionExpression conditionExpression;

    public ProcessActivity getSourceActivity() {
        return sourceActivity;
    }

    public void setSourceActivity(ProcessActivity sourceActivity) {
        this.sourceActivity = sourceActivity;
    }

    public ProcessActivity getTargetActivity() {
        return targetActivity;
    }

    public void setTargetActivity(ProcessActivity targetActivity) {
        this.targetActivity = targetActivity;
    }

    public RestrictionExpression getConditionExpression() {
        return conditionExpression;
    }

    public void setConditionExpression(RestrictionExpression conditionExpression) {
        this.conditionExpression = conditionExpression;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}
