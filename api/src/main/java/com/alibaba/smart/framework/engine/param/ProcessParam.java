package com.alibaba.smart.framework.engine.param;

import lombok.Getter;
import lombok.Setter;

/**
 * 流程实例入参
 * Created by dongdongzdd on 16/8/14.
 */
public class ProcessParam extends Param {

    private static String DEFAULT_VERSION = "1.0";


    @Setter
    private String processId;

    @Getter
    private String parentProcessId;


    @Getter
    @Setter
    private String processDefationId;


    @Getter
    @Setter
    private String processDefationVersion = DEFAULT_VERSION;


    @Override
    public String getProcessId() {
        return this.processId;
    }
}
