package com.alibaba.smart.framework.engine.param;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.util.EngineConstant;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
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
    private List<ExecutionParam> executionParams = Lists.newArrayList();

    @Getter
    @Setter
    private List<ActivityParam> activityParams = Lists.newArrayList();


    @Getter
    @Setter
    Map<String,Object> processContext;


    public static EngineParam of(String processId,String defationId,String version,String executionString) {

        if (null == executionString || null == processId|| null == defationId|| null == version) {
            throw new EngineException("param can not be null");
        }
        EngineParam param = new EngineParam();

        ProcessParam process = new ProcessParam();


        process.setProcessId(processId);


        process.setProcessDefationId(defationId);
        process.setProcessDefationVersion(version);

        String[] executions = executionString.split(EngineConstant.REG_SEP_G);
        Arrays.stream(executions).forEach(
                p->{
                    if (p.equals("abort")) {
                        throw new EngineException("the process instance is alreay abort,engine will not excute ");
                    }
                    ExecutionParam execution = new ExecutionParam();
                    execution.setProcessId(processId);
                    ActivityParam activity = new ActivityParam();
                    activity.setProceessId(processId);
                    String[] executionParams = p.split(EngineConstant.REG_SEP_S);
                    if (executionParams.length < 2) {
                        throw new EngineException("execution param is not right");
                    }
                    execution.setExecutionId(executionParams[0]);
                    execution.setActivityId(executionParams[1]);
                    activity.setActivityId(executionParams[1]);
                    if (executionParams.length >2) {
                        activity.setCurrentStep(executionParams[2]);
                    }
                    param.getActivityParams().add(activity);
                    param.getExecutionParams().add(execution);
                }
        );



        param.setProcessParam(process);

        return param;




    }


}
