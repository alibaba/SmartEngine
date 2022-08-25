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






}
