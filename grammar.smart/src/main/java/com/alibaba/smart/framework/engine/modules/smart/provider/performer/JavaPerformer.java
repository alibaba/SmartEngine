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

    /**
     * @author 高海军 帝奇 74394 on 2017 February  09:32: TccDelegationUtil
     *
     */
    @Override
    public Object perform(ExecutionContext context) {

        ProcessEngineConfiguration processEngineConfiguration = context.getProcessEngineConfiguration();
        ExceptionProcessor exceptionProcessor = processEngineConfiguration.getExceptionProcessor();

        if (this.target instanceof JavaDelegation) {
            JavaDelegation javaDelegation = (JavaDelegation)this.target;
            return javaDelegation.execute(context);

        }
        if (this.target instanceof TccDelegation) {
            //TODO TCC只实现了try,rewview by ettear
            TccDelegation tccDelegation = (TccDelegation)this.target;

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
            throw new EngineException("The delegation not support : " + target.getClass());
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
