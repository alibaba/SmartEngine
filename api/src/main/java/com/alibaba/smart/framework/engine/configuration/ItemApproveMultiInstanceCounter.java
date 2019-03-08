package com.alibaba.smart.framework.engine.configuration;

import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.Activity;

public interface ItemApproveMultiInstanceCounter {

    Long countPassedTaskItemInstanceNumber(String processInstanceId, String activityInstanceId, String subBizId, SmartEngine smartEngine);

    Long countRejectedTaskItemInstanceNumber(String processInstanceId, String activityInstanceId, String subBizId, SmartEngine smartEngine);

    /**
     * 是否可以驱动到下一个主节点
     * 返回值： key=canDrive(是否可以驱动到下一个节点)  key=tag(如可以驱动到下一个节点，则该节点的审核结果的标签（tag）)
     * 例子：Map<String,String> map = new HashMap<>;
     *      map.put("canDrive","true");
     *      map.put("tag","pass");
     */
    Map<String,String> canDriveNextMainElement(String processInstanceId, Activity activity, SmartEngine smartEngine);
}
