package com.alibaba.smart.framework.engine.modules.extensions.transaction.node;


import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractActivity;
import com.alibaba.smart.framework.engine.modules.smart.assembly.SmartBase;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Leo.yy   Created on 17/7/31.
 * @description
 * @see
 */
public class TransactionTask extends AbstractActivity {

    private static final long serialVersionUID = -1l;

    public final static QName artifactType = new QName(SmartBase.SMART_NS, "transactionTask");

    private ErrorStrategy errorStrategy;

    private String metaTopic;
    private String metaGroup;

    private String mode;

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

    public String getMetaTopic() {
        return metaTopic;
    }

    public void setMetaTopic(String metaTopic) {
        this.metaTopic = metaTopic;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMetaGroup() {
        return metaGroup;
    }

    public void setMetaGroup(String metaGroup) {
        this.metaGroup = metaGroup;
    }
}
