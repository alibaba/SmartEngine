package com.alibaba.smart.framework.engine.modules.extensions.transaction.node;

import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractActivity;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.action.SingleTaskAction;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.util.SpringContextUtil;

import javax.xml.namespace.QName;

/**
 * @author Leo.yy   Created on 2017/8/2.
 * @description
 * @see
 */
public class SingleTask extends AbstractActivity {

    private static final long serialVersionUID = -1l;


    public final static QName artifactType = new QName(SmartExtBase.SMART_EXT_NS, "singleTask");

    private String actionBeanName;
    private ErrorStrategy errorStrategy;

    public String getActionBeanName() {
        return actionBeanName;
    }

    public void setActionBeanName(String actionBeanName) {
        this.actionBeanName = actionBeanName;
    }

    public SingleTaskAction getAction() {
        return (SingleTaskAction) SpringContextUtil.getBean(actionBeanName);
    }

    public ErrorStrategy getErrorStrategy() {
        return errorStrategy;
    }

    public void setErrorStrategy(ErrorStrategy errorStrategy) {
        this.errorStrategy = errorStrategy;
    }

}
