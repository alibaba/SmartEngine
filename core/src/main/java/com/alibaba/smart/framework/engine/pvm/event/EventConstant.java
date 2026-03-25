package com.alibaba.smart.framework.engine.pvm.event;


public enum EventConstant {

    start,// 流程实例开始或者环节节点进入 ,类似于 PROCESS_START ACTIVITY_START
    end, // 流程实例结束或者环节节点离开 ,类似于 PROCESS_END ACTIVITY_END

    take, // 互斥网关的sequenceFlow被选中执行时触发

    // above is new support event

    PROCESS_START,
    PROCESS_END,

    ACTIVITY_START,
    ACTIVITY_EXECUTE,
    ACTIVITY_END,

    // Task lifecycle events (fired via TaskEventPublisher SPI)
    TASK_ASSIGNED,       // Task created and assigned to candidate(s)
    TASK_CLAIMED,        // Task claimed by a specific user
    TASK_COMPLETED,      // Task completed (approved/processed)
    TASK_CANCELED,       // Task canceled (e.g. countersign decision made by others)
    TASK_TRANSFERRED,    // Task transferred to another user
    TASK_DELEGATED,      // New assignee added to task
    TASK_REVOKED,        // Assignee removed from task



}
