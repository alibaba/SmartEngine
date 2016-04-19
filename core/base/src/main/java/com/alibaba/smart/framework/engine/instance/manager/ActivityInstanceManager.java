package com.alibaba.smart.framework.engine.instance.manager;

import com.alibaba.smart.framework.engine.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.instance.ExecutionInstance;

import java.util.List;

/**
 * Created by ettear on 16-4-19.
 */
public interface ActivityInstanceManager {
    ExecutionInstance create(ActivityInstance activityInstance);

}
