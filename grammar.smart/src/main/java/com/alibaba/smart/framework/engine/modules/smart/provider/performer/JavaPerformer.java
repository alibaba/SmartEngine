package com.alibaba.smart.framework.engine.modules.smart.provider.performer;

import com.alibaba.smart.framework.engine.common.processor.ExceptionProcessor;
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
    private Object target;

    public JavaPerformer(ExtensionPointRegistry extensionPointRegistry, Object target) {
        this.target = target;
    }

    @Override
    public Object perform(ExecutionContext context) {
        boolean errorOccurred = false;

        ProcessEngineConfiguration processEngineConfiguration = context.getProcessEngineConfiguration();
        ExceptionProcessor exceptionProcessor = processEngineConfiguration.getExceptionProcessor();

        if (this.target instanceof JavaDelegation) {
            JavaDelegation javaDelegation = (JavaDelegation)this.target;
            return javaDelegation.execute(context);

        }
        if (this.target instanceof TccDelegation) {
            TccDelegation tccDelegation = (TccDelegation)this.target;

            TccResult tccResult = null;
            try {
                tccResult = tccDelegation.tryExecute(context);
            } catch (Exception e) {
                errorOccurred = dealException(exceptionProcessor, e);
            }

            if (tccResult != null) {
                if (tccResult.isSucessful()) {
                    //do nothing
                } else {

                    Object target = tccResult.getTarget();
                    Exception exception = new Exception(target.toString());

                    errorOccurred = dealException(exceptionProcessor, exception);

                }
            }

        } else {
            throw new EngineException("The delegation not support : " + target.getClass());
        }
        return errorOccurred;
    }

    private static boolean dealException(ExceptionProcessor exceptionProcessor, Exception exception) {

        if (null != exceptionProcessor) {
            exceptionProcessor.process(exception);
        } else if (exception instanceof RuntimeException) {
            throw (RuntimeException)exception;
        } else {
            throw new EngineException(exception);
        }
        return true;

    }
}
