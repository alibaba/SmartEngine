package com.alibaba.smart.framework.engine.configuration.impl;

import java.util.Map;

import com.alibaba.smart.framework.engine.annotation.Retryable;
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

import com.alibaba.smart.framework.engine.util.ThreadPoolUtil;
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
                LOGGER.debug("No behavior found:"+activity.getId());
            }
        }
    }

    protected   void execute(ExecutionContext context, String className, Activity activity) {
        ProcessEngineConfiguration processEngineConfiguration = context.getProcessEngineConfiguration();
        ExceptionProcessor exceptionProcessor = processEngineConfiguration.getExceptionProcessor();

        InstanceAccessor instanceAccessor = processEngineConfiguration
            .getInstanceAccessor();
        Object delegation = instanceAccessor.access(className);

        boolean present =  delegation.getClass().isAnnotationPresent(Retryable.class);


            if (delegation instanceof ContextBoundedJavaDelegation) {
                ContextBoundedJavaDelegation contextBoundedJavaDelegation = (ContextBoundedJavaDelegation)delegation;
                contextBoundedJavaDelegation.setClassName(className);
                contextBoundedJavaDelegation.setActivity(activity);

                execute(context, contextBoundedJavaDelegation,  exceptionProcessor , present);

            } else if (delegation instanceof JavaDelegation) {
                JavaDelegation javaDelegation = (JavaDelegation)delegation;
                execute(context, javaDelegation,  exceptionProcessor ,present);

            } else if (delegation instanceof TccDelegation) {

                TccDelegation tccDelegation = (TccDelegation)delegation;

                tccDelegation.tryExecute(context);

            } else {
                throw new EngineException("The delegation is not support : " + delegation.getClass());
            }



    }

    protected void execute(ExecutionContext context, JavaDelegation delegation,ExceptionProcessor exceptionProcessor ,boolean present) {


        if(!present){
            delegation.execute(context);
        }else {
            Retryable annotation = delegation.getClass().getAnnotation(Retryable.class);
            long delay = annotation.delay();
            int maxAttempts = annotation.maxAttempts();

            int attemptCount = 0;
            for (;  attemptCount < maxAttempts; attemptCount++) {

                boolean success = false;
                try {
                    delegation.execute(context);
                    success = true;
                }catch (Exception e){
                    dealException(exceptionProcessor, e,context);
                    ThreadPoolUtil.sleepSilently(delay);
                }

                if(success){
                    break;
                }


            }

            if(attemptCount == maxAttempts -1){
                // means all retry failed
                // record log ,trigger alert, persist params if needed
            }
        }
    }


    protected   void dealException(ExceptionProcessor exceptionProcessor, Exception exception,ExecutionContext context) {

        if (null != exceptionProcessor) {
            exceptionProcessor.process(exception,context);
        } else if (exception instanceof RuntimeException) {
            throw (RuntimeException)exception;
        } else {
            throw new EngineException(exception);
        }
    }


}