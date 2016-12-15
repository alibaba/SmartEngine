package com.alibaba.smart.framework.engine.param;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.util.EngineConstant;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
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


    public static EngineParam of(String processId,String defationId,String version,String executionString) {

        EngineParam param = new EngineParam();
        ExecutionParam execution = new ExecutionParam();
        ProcessParam process = new ProcessParam();
        ActivityParam activity = new ActivityParam();

        process.setProcessId(processId);
        execution.setProcessId(processId);
        activity.setProceessId(processId);

        process.setProcessDefationId(defationId);
        if (version != null) {
            process.setProcessDefationVersion(version);

        }

        String[] executionParams = executionString.split(EngineConstant.REG_SEP_S);
        if (executionParams.length < 2) {
            throw new EngineException("execution param is not right");
        }
        if (Arrays.stream(executionParams).anyMatch(p->p.equals("abort"))) {
            throw new EngineException("the process instance is alreay abort,engine will not excute ");
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
