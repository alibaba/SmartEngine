package com.alibaba.smart.framework.engine.pvm.event;

/**
 * Created by ettear on 16-4-19.
 */
public enum PvmEventConstant {
    PROCESS_START(1),
    PROCESS_END(2),
    
    ACTIVITY_START(3),
    ACTIVITY_EXECUTE(4),
    ACTIVITY_END(5),
    
    ACTIVITY_TRANSITION_SELECT(6),
    TRANSITION_HIT(7),
    TRANSITION_START(8),
    TRANSITION_EXECUTE(9),
    TRANSITION_END(10),


    PROCESS_PUSH(11)
    ;



    private int code;

    PvmEventConstant(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static String getName(int i) {
        for (PvmEventConstant eventConstant: PvmEventConstant.values()) {
            if (eventConstant.getCode() == i) {
                return eventConstant.name();
            }
        }
        return null;
    }
}
