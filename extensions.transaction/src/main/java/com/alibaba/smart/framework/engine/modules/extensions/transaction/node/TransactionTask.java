package com.alibaba.smart.framework.engine.modules.extensions.transaction.node;

import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.AbstractBpmnActivity;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Leo.yy   Created on 17/7/31.
 * @description
 * @see
 */
public class TransactionTask extends AbstractBpmnActivity {

    private static final long serialVersionUID = -1l;


    public final static QName artifactType = new QName(SmartExtBase.SMART_EXT_NS, "transactionTask");

    private ErrorStrategy errorStrategy;

    private int maxRedoCount = 10;


    private List<SingleTask> childTasks = new ArrayList<SingleTask>();

    public List<SingleTask> getChildTasks() {
        return childTasks;
    }


    public int getMaxRedoCount() {
        return maxRedoCount;
    }

    public void setMaxRedoCount(int maxRedoCount) {
        this.maxRedoCount = maxRedoCount;
    }

    public void setChildTasks(List<SingleTask> childTasks) {
        this.childTasks = childTasks;
    }

    public ErrorStrategy getErrorStrategy() {
        return errorStrategy;
    }

    public void setErrorStrategy(ErrorStrategy errorStrategy) {
        this.errorStrategy = errorStrategy;
    }


}
