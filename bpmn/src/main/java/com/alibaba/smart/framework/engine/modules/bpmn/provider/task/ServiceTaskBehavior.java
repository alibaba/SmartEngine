package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;

import com.alibaba.smart.framework.engine.configuration.ExceptionProcessor;
import com.alibaba.smart.framework.engine.configuration.InstanceAccessor;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.ContextBoundedJavaDelegation;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ReceiveTask;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ServiceTask;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtensionBinding(type = ExtensionConstant.ACTIVITY_BEHAVIOR,binding = ServiceTask.class)

public class ServiceTaskBehavior extends AbstractActivityBehavior<ServiceTask> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTaskBehavior.class);


    public ServiceTaskBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }

    public ServiceTaskBehavior() {
        super();
    }

    @Override
    public boolean execute(ExecutionContext context) {
        //tune

        ServiceTask model = this.getModel();
        String className  =  model.getProperties().get("class");

        ProcessEngineConfiguration processEngineConfiguration = context.getProcessEngineConfiguration();
        ExceptionProcessor exceptionProcessor = processEngineConfiguration.getExceptionProcessor();

        InstanceAccessor instanceAccessor = processEngineConfiguration
            .getInstanceAccessor();
        Object delegation = instanceAccessor.access(className);


        if (delegation instanceof ContextBoundedJavaDelegation) {
            ContextBoundedJavaDelegation contextBoundedJavaDelegation = (ContextBoundedJavaDelegation)delegation;
            contextBoundedJavaDelegation.setClassName(className);
            contextBoundedJavaDelegation.setExtensionPointRegistry(extensionPointRegistry);
            contextBoundedJavaDelegation.setPvmElement(this.getPvmActivity());

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

        return false;
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
