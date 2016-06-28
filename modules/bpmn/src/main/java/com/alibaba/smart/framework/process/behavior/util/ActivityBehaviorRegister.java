package com.alibaba.smart.framework.process.behavior.util;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.EndEvent;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.StartEvent;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ExclusiveGateway;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ParallelGateway;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ServiceTask;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.UserTask;
import com.alibaba.smart.framework.process.behavior.ActivityBehavior;
import com.alibaba.smart.framework.process.behavior.event.EndEventBehavior;
import com.alibaba.smart.framework.process.behavior.event.StartEventBehavior;
import com.alibaba.smart.framework.process.behavior.gateway.ExclusiveGatewayBehavior;
import com.alibaba.smart.framework.process.behavior.gateway.ParallelGatewayBehavior;
import com.alibaba.smart.framework.process.behavior.task.ServiceTaskBehavior;
import com.alibaba.smart.framework.process.behavior.task.UserTaskBehavior;

public class ActivityBehaviorRegister {

    private ActivityBehaviorRegister() {
    }

    private static Map<String, ActivityBehavior> holder = new HashMap<>();

    // TODO 确定下是否有更好的方式,使用扩展点或者约定的方式
    static {
        // TODO add 唯一性check
        holder.put(StartEvent.class.getName(), new StartEventBehavior());
        holder.put(EndEvent.class.getName(), new EndEventBehavior());

        holder.put(ServiceTask.class.getName(), new ServiceTaskBehavior());
        holder.put(UserTask.class.getName(), new UserTaskBehavior());

        holder.put(ExclusiveGateway.class.getName(), new ExclusiveGatewayBehavior());
        holder.put(ParallelGateway.class.getName(), new ParallelGatewayBehavior());

    }

    public static ActivityBehavior getActivityBehavior(String className) {
        ActivityBehavior activityBehavior = holder.get(className);
        if (null == activityBehavior) {
            throw new RuntimeException("No ActivityBehavior found for " + className);
        }

        return activityBehavior;

    }
}
