package com.alibaba.smart.framework.engine.param;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.util.EngineConstant;
import lombok.Getter;
import lombok.Setter;

/**
 * 执行参数
 * Created by dongdongzdd on 16/8/11.
 */

public class ExecutionParam extends Param {

    @Getter
    @Setter
    private String activityId;

    @Getter
    @Setter
    private String executionId;

    @Setter
    private String processId;







    @Override
    public String getProcessId() {
        return this.processId;
    }
}
