package com.alibaba.smart.framework.process.model.bpmn;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.process.model.bpmn.sequenceflow.ProcessSequenceFlow;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
public class ProcessActivity extends BaseProcessNode {

    private List<ProcessSequenceFlow> incomingFlowList  = new ArrayList<ProcessSequenceFlow>(4);
    private List<ProcessSequenceFlow> outcomingFlowList = new ArrayList<ProcessSequenceFlow>(4);

    public List<ProcessSequenceFlow> getIncomingFlowList() {
        return incomingFlowList;
    }

    public void setIncomingFlowList(List<ProcessSequenceFlow> incomingFlowList) {
        this.incomingFlowList = incomingFlowList;
    }

    public List<ProcessSequenceFlow> getOutcomingFlowList() {
        return outcomingFlowList;
    }

    public void setOutcomingFlowList(List<ProcessSequenceFlow> outcomingFlowList) {
        this.outcomingFlowList = outcomingFlowList;
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
