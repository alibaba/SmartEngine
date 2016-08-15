package com.alibaba.smart.framework.engine.invocation;

/**
 * Created by ettear on 16-4-19.
 */
public enum AtomicOperationEventConstant {
    PROCESS_START(1),
    PROCESS_END(2),
    ACTIVITY_START(3),
    ACTIVITY_EXECUTE(4),
    ACTIVITY_END(5),
    ACTIVITY_TRANSITION_SELECT(6),
    TRANSITION_HIT(7),
    TRANSITION_START(8),
    TRANSITION_EXECUTE(9),
    TRANSITION_END(10);


    private int code;

    AtomicOperationEventConstant(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static String getName(int i) {
        for (AtomicOperationEventConstant eventConstant: AtomicOperationEventConstant.values()) {
            if (eventConstant.getCode() == i) {
                return eventConstant.name();
            }
        }
        return null;
    }
}
