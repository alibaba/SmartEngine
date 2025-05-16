package com.alibaba.smart.framework.engine.persister.database.builder;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.instance.impl.DefaultActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.persister.database.entity.ActivityInstanceEntity;

import java.util.Date;


/**
 * @author yanricheng@163.com
 * @date 2025/05/015
 */
public final class ActivityInstanceBuilder {
    public static ActivityInstanceEntity buildActivityInstanceEntity(ActivityInstance activityInstance) {
        ActivityInstanceEntity activityInstanceEntityToBePersisted = new ActivityInstanceEntity();
        activityInstanceEntityToBePersisted.setProcessDefinitionIdAndVersion(activityInstance.getProcessDefinitionIdAndVersion());

        activityInstanceEntityToBePersisted.setProcessDefinitionActivityId(activityInstance.getProcessDefinitionActivityId());
        activityInstanceEntityToBePersisted.setProcessInstanceId(Long.valueOf(activityInstance.getProcessInstanceId()));
        activityInstanceEntityToBePersisted.setId(Long.valueOf(activityInstance.getInstanceId()));

        Date currentDate = DateUtil.getCurrentDate();
        activityInstanceEntityToBePersisted.setGmtCreate(currentDate);
        activityInstanceEntityToBePersisted.setGmtModified(currentDate);
        activityInstanceEntityToBePersisted.setTenantId(activityInstance.getTenantId());

        return activityInstanceEntityToBePersisted;
    }

    public static ActivityInstance buildActivityInstanceFromEntity(ActivityInstanceEntity activityInstanceEntity) {
        ActivityInstance activityInstance = new DefaultActivityInstance();

        activityInstance.setStartTime(activityInstanceEntity.getGmtCreate());
        ((DefaultActivityInstance) activityInstance).setCompleteTime(activityInstanceEntity.getGmtModified());

        activityInstance.setProcessDefinitionIdAndVersion(activityInstanceEntity.getProcessDefinitionIdAndVersion());
        activityInstance.setInstanceId(activityInstanceEntity.getId().toString());
        activityInstance.setProcessInstanceId(activityInstanceEntity.getProcessInstanceId().toString());
        activityInstance.setProcessDefinitionActivityId(activityInstanceEntity.getProcessDefinitionActivityId());
        activityInstance.setTenantId(activityInstanceEntity.getTenantId());
        return activityInstance;
    }
}
