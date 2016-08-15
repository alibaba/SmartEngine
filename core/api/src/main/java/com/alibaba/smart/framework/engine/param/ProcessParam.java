package com.alibaba.smart.framework.engine.param;

import lombok.Getter;
import lombok.Setter;

/**
 * 流程实例入参
 * Created by dongdongzdd on 16/8/14.
 */
public class ProcessParam extends Param {


    @Setter
    private String processId;

    @Getter
    private String parentProcessId;



    @Override
    public String getProcessId() {
        return this.processId;
    }
}
