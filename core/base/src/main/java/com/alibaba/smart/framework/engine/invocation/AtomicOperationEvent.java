package com.alibaba.smart.framework.engine.invocation;

/**
 * Created by ettear on 16-4-19.
 */
public enum  AtomicOperationEvent {
    PROCESS_START,
    PROCESS_END,

    ACTIVITY_START,
    ACTIVITY_EXECUTE,
    ACTIVITY_END,
    ACTIVITY_TRANSITION_SELECT,

    TRANSITION_HIT,
    TRANSITION_START,
    TRANSITION_EXECUTE,
    TRANSITION_END,
}
