package com.alibaba.smart.framework.engine.delegation;

import java.util.Map;

import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.configuration.ExceptionProcessor;
import com.alibaba.smart.framework.engine.configuration.InstanceAccessor;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.ContextBoundedJavaDelegation;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

/**
 * Created by 高海军 帝奇 74394 on  2019-08-30 16:08.
 */
public class BehaviorUtil {

    public static    void executionBehaviorIf(ExecutionContext context, Map<String, String> properties,ExtensionPointRegistry extensionPointRegistry,
                                              PvmActivity pvmActivity) {

        //TUNE 允许扩展
        if(MapUtil.isNotEmpty(properties)){
            String className  =  properties.get("class");
            if(null != className){
                behavior(context, className,extensionPointRegistry,pvmActivity);
            }else {
                //tune logger
            }
        }
    }

    private static void behavior(ExecutionContext context, String className, ExtensionPointRegistry extensionPointRegistry,
                                PvmActivity pvmActivity) {
        ProcessEngineConfiguration processEngineConfiguration = context.getProcessEngineConfiguration();
        ExceptionProcessor exceptionProcessor = processEngineConfiguration.getExceptionProcessor();

        InstanceAccessor instanceAccessor = processEngineConfiguration
            .getInstanceAccessor();
        Object delegation = instanceAccessor.access(className);
        //TUNE 允许扩展
        if (delegation instanceof ContextBoundedJavaDelegation) {
            ContextBoundedJavaDelegation contextBoundedJavaDelegation = (ContextBoundedJavaDelegation)delegation;
            contextBoundedJavaDelegation.setClassName(className);
            contextBoundedJavaDelegation.setExtensionPointRegistry(extensionPointRegistry);
            contextBoundedJavaDelegation.setPvmElement(pvmActivity);

            contextBoundedJavaDelegation.execute(context);

        } else if (delegation instanceof JavaDelegation) {
            JavaDelegation javaDelegation = (JavaDelegation)delegation;
            javaDelegation.execute(context);

        } else if (delegation instanceof TccDelegation) {
            //TODO TCC目前只实现了try
            TccDelegation tccDelegation = (TccDelegation)delegation;

            TccResult tccResult = null;
            try {
                tccResult = tccDelegation.tryExecute(context);
            } catch (Exception e) {
                dealException(exceptionProcessor, e,context);
            }

            if (tccResult != null) {
                if (tccResult.isSucessful()) {
                    tccResult.getTarget();
                } else {

                    Object target = tccResult.getTarget();
                    Exception exception = new Exception(target.toString());

                    dealException(exceptionProcessor, exception,context);

                }
            }

        } else {
            throw new EngineException("The delegation not support : " + delegation.getClass());
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