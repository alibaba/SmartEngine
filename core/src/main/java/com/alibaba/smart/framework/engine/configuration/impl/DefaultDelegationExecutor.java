package com.alibaba.smart.framework.engine.configuration.impl;

import java.util.Map;

import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.configuration.DelegationExecutor;
import com.alibaba.smart.framework.engine.configuration.ExceptionProcessor;
import com.alibaba.smart.framework.engine.configuration.InstanceAccessor;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.ContextBoundedJavaDelegation;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.model.assembly.Activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 高海军 帝奇 74394 on  2019-08-30 16:08.
 */
public class DefaultDelegationExecutor implements DelegationExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDelegationExecutor.class);

    @Override
    public void execute(ExecutionContext context, Activity activity) {

        Map<String, String> properties = activity.getProperties();
        if(MapUtil.isNotEmpty(properties)){
            String className  =  properties.get("class");
            if(null != className){
                execute(context, className,activity);
            }else {
                LOGGER.info("No behavior found:"+activity.getId());
            }
        }
    }

    private static void execute(ExecutionContext context, String className, Activity activity) {
        ProcessEngineConfiguration processEngineConfiguration = context.getProcessEngineConfiguration();
        ExceptionProcessor exceptionProcessor = processEngineConfiguration.getExceptionProcessor();

        InstanceAccessor instanceAccessor = processEngineConfiguration
            .getInstanceAccessor();
        Object delegation = instanceAccessor.access(className);

        try{
            if (delegation instanceof ContextBoundedJavaDelegation) {
                ContextBoundedJavaDelegation contextBoundedJavaDelegation = (ContextBoundedJavaDelegation)delegation;
                contextBoundedJavaDelegation.setClassName(className);
                contextBoundedJavaDelegation.setActivity(activity);

                contextBoundedJavaDelegation.execute(context);

            } else if (delegation instanceof JavaDelegation) {
                JavaDelegation javaDelegation = (JavaDelegation)delegation;
                javaDelegation.execute(context);

            } else if (delegation instanceof TccDelegation) {

                TccDelegation tccDelegation = (TccDelegation)delegation;

                tccDelegation.tryExecute(context);

            } else {
                throw new EngineException("The delegation is not support : " + delegation.getClass());
            }

        }catch (Exception e){
            dealException(exceptionProcessor, e,context);
        }

    }

    private static void dealException(ExceptionProcessor exceptionProcessor, Exception exception,ExecutionContext context) {

        if (null != exceptionProcessor) {
            exceptionProcessor.process(exception,context);
        } else if (exception instanceof RuntimeException) {
            throw (RuntimeException)exception;
        } else {
            throw new EngineException(exception);
        }
    }


}