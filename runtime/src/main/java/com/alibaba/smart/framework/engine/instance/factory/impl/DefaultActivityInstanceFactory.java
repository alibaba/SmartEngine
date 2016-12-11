package com.alibaba.smart.framework.engine.instance.factory.impl;

import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultActivityInstance;
import com.alibaba.smart.framework.engine.instance.util.InstanceIdUtil;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.param.ActivityParam;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.util.DateUtil;

/**
 * 默认活动实例工厂实现 Created by ettear on 16-4-20.
 */
public class DefaultActivityInstanceFactory implements ActivityInstanceFactory {

    @Override
    public ActivityInstance create(PvmActivity pvmActivity, ProcessInstance processInstance) {
        DefaultActivityInstance activityInstance = new DefaultActivityInstance();
        activityInstance.setInstanceId(InstanceIdUtil.simpleId());
        activityInstance.setStartDate(DateUtil.getCurrentDate());
        activityInstance.setProcessInstanceId(processInstance.getInstanceId());

        String activityId = pvmActivity.getModel().getId();
        activityInstance.setActivityId(activityId);
        return activityInstance;
    }

//    @Override
//    public ActivityInstance recovery(ActivityParam activityParam) {
//        DefaultActivityInstance defaultActivityInstance = new DefaultActivityInstance();
//        defaultActivityInstance.getModel(activityParam);
//
//        return defaultActivityInstance;
//
//    }
}
