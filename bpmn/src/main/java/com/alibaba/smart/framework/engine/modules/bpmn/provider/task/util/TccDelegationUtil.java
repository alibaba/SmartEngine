package com.alibaba.smart.framework.engine.modules.bpmn.provider.task.util;

import com.alibaba.smart.framework.engine.common.processor.ExceptionProcessor;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.instance.util.ClassLoaderUtil;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  09:32.
 */
public abstract  class TccDelegationUtil {


    public static void execute(ExecutionContext executionContext, String className) {

        ProcessEngineConfiguration processEngineConfiguration = executionContext.getProcessEngineConfiguration();
        ExceptionProcessor exceptionProcessor= processEngineConfiguration.getExceptionProcessor();


        // TODO need cache,rename
        Object serviceTaskDelegation = ClassLoaderUtil.createNewInstance(className);
        if (serviceTaskDelegation instanceof TccDelegation) {
            TccDelegation tccDelegation = (TccDelegation) serviceTaskDelegation;

            TccResult tccResult = null;
            try {
                tccResult = tccDelegation.tryExecute(executionContext);
            } catch (Exception e) {
                dealException(exceptionProcessor, e);
            }

            if(tccResult != null){
                if(tccResult.isSucessful()){
                    //do nothing
                }else{

                    Object target = tccResult.getTarget();
                    Exception exception = new Exception(target.toString());

                    dealException(exceptionProcessor, exception);

                }
            }

        }
    }

    private static void dealException(ExceptionProcessor exceptionProcessor, Exception exception)  {
        if(null != exceptionProcessor){
            exceptionProcessor.process(exception);
        }else{
            throw new EngineException(exception);
        }
    }
}
