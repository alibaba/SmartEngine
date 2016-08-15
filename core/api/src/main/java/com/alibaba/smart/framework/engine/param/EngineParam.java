package com.alibaba.smart.framework.engine.param;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.util.EngineConstant;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 流程引擎入参
 * Created by dongdongzdd on 16/8/15.
 */
public class EngineParam {

    @Getter
    @Setter
    private ProcessParam processParam;


    @Getter
    @Setter
    private ExecutionParam executionParam;

    @Getter
    @Setter
    private ActivityParam activityParam;


    @Getter
    @Setter
    Map<String,Object> processContext;


    public static EngineParam of(String processId,String executionString) {

        EngineParam param = new EngineParam();
        ExecutionParam execution = new ExecutionParam();
        ProcessParam process = new ProcessParam();
        ActivityParam activity = new ActivityParam();

        process.setProcessId(processId);
        execution.setProcessId(processId);
        activity.setProceessId(processId);

        String[] executionParams = executionString.split(EngineConstant.REG_SEP_S);
        if (executionParams.length < 2) {
            throw new EngineException("execution param is not right");
        }
        execution.setExecutionId(executionParams[0]);

        activity.setActivityId(executionParams[1]);
        if (executionParams.length >2) {
            activity.setCurrentStep(executionParams[2]);
        }

        param.setActivityParam(activity);
        param.setExecutionParam(execution);
        param.setProcessParam(process);

        return param;




    }


}
