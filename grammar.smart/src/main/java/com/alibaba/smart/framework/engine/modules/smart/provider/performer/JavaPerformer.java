package com.alibaba.smart.framework.engine.modules.smart.provider.performer;

import com.alibaba.smart.framework.engine.configuration.ExceptionProcessor;
import com.alibaba.smart.framework.engine.configuration.InstanceAccessor;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.provider.Performer;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class JavaPerformer implements Performer {
    private String className;

    public JavaPerformer(ExtensionPointRegistry extensionPointRegistry, String className) {
        this.className = className;
    }

    /**
     * @author 高海军 帝奇 74394 on 2017 February  09:32: TccDelegationUtil
     *
     */
    @Override
    public Object perform(ExecutionContext context) {

        ProcessEngineConfiguration processEngineConfiguration = context.getProcessEngineConfiguration();
        ExceptionProcessor exceptionProcessor = processEngineConfiguration.getExceptionProcessor();

        InstanceAccessor instanceAccessor = processEngineConfiguration
            .getInstanceAccessor();
        Object delegation = instanceAccessor.access(this.className);


        if (delegation instanceof JavaDelegation) {
            JavaDelegation javaDelegation = (JavaDelegation)delegation;
            return javaDelegation.execute(context);

        }else if (delegation instanceof TccDelegation) {
            //TODO TCC只实现了try,rewview by ettear
            TccDelegation tccDelegation = (TccDelegation)delegation;

            TccResult tccResult = null;
            try {
                tccResult = tccDelegation.tryExecute(context);
            } catch (Exception e) {
                dealException(exceptionProcessor, e,context);
            }

            if (tccResult != null) {
                if (tccResult.isSucessful()) {
                    return tccResult.getTarget();
                } else {

                    Object target = tccResult.getTarget();
                    Exception exception = new Exception(target.toString());

                    dealException(exceptionProcessor, exception,context);

                }
            }

        } else {
            throw new EngineException("The delegation not support : " + delegation.getClass());
        }
        return null;
    }

    /**
     * @author 高海军 帝奇 74394 on 2017 February  09:32: TccDelegationUtil
     */
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
