package com.alibaba.smart.framework.engine.pvm.event;

/**
 * Created by ettear on 16-4-19.
 */
public enum PvmEventConstant {
    PROCESS_START, PROCESS_END,

    ACTIVITY_START, ACTIVITY_EXECUTE, ACTIVITY_END, ACTIVITY_TRANSITION_SELECT,

    TRANSITION_HIT, TRANSITION_START, TRANSITION_EXECUTE, TRANSITION_END,
}
