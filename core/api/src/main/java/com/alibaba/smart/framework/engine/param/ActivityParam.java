package com.alibaba.smart.framework.engine.param;

import lombok.Getter;
import lombok.Setter;

/**
 * 节点参数
 * Created by dongdongzdd on 16/8/15.
 */
public class ActivityParam extends Param {

    @Getter
    @Setter
    private String activityId;

    @Setter
    private String proceessId;

    @Getter
    @Setter
    private String currentStep;

    @Override
    public String getProcessId() {
        return proceessId;
    }
}
