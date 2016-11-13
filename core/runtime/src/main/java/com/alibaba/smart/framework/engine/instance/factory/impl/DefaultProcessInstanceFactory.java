package com.alibaba.smart.framework.engine.instance.factory.impl;

import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultProcessInstance;
import com.alibaba.smart.framework.engine.instance.util.InstanceIdUtil;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.param.ProcessParam;
import com.alibaba.smart.framework.engine.util.DateUtil;

/**
 * 默认流程实例工厂实现 Created by ettear on 16-4-20.
 */
public class DefaultProcessInstanceFactory implements ProcessInstanceFactory {

    @Override
    public ProcessInstance create() {
        DefaultProcessInstance defaultProcessInstance = new DefaultProcessInstance();
        defaultProcessInstance.setInstanceId(InstanceIdUtil.uuid());
        defaultProcessInstance.setStatus(InstanceStatus.running);
        defaultProcessInstance.setStartDate(DateUtil.getCurrentDate());

        return defaultProcessInstance;
    }

    //todo 子流程
    @Override
    public ProcessInstance recovery(ProcessParam param) {
        DefaultProcessInstance defaultProcessInstance  = new DefaultProcessInstance();
        defaultProcessInstance.getModel(param);
        return defaultProcessInstance;
    }


}
