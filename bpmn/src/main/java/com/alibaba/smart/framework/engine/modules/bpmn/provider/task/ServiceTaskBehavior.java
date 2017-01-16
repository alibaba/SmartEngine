package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;

import com.alibaba.smart.framework.engine.common.processor.ExceptionProcessor;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.util.ClassLoaderUtil;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.action.Action;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ServiceTask;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceTaskBehavior extends AbstractActivityBehavior<ServiceTask> implements ActivityBehavior<ServiceTask> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTaskBehavior.class);


    public ServiceTaskBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }


    @Override
    public void buildInstanceRelationShip(PvmActivity pvmActivity, ExecutionContext executionContext) {
        ProcessInstance processInstance = executionContext.getProcessInstance();
        ActivityInstance activityInstance = super.activityInstanceFactory.createWithBlockId(pvmActivity, executionContext);

        ExecutionInstance executionInstance = super.executionInstanceFactory.create(activityInstance);
        activityInstance.setExecutionInstance(executionInstance);

        processInstance.addNewActivityInstance(activityInstance);



        ProcessEngineConfiguration processEngineConfiguration = executionContext.getProcessEngineConfiguration();
        ExceptionProcessor exceptionProcessor= processEngineConfiguration.getExceptionProcessor();


        ServiceTask serviceTask = (ServiceTask) pvmActivity.getModel();
        String className = serviceTask.getClassName();

        //TODO
        if (null == className) {

            Action action = serviceTask.getAction();
            if (action == null) {
                return;
            }

            if (action.getType().equals("spring")) {
//                SpringAction springAction = new SpringAction(action.getId(),action.getMethod(),executionContext.getRequest());
//                try {
//                    // TODO 考虑下是否需要给对应的返回值,和 wufeng 确认
//                     springAction.buildInstanceRelationShip();
//                } catch (Throwable e) {
//                    //这里好像抓不到异常了已经
//                }


            }
        } else {
            // TODO need cache,rename
            Object serviceTaskDelegation = ClassLoaderUtil.createNewInstance(className);
            if (serviceTaskDelegation instanceof TccDelegation) {
                TccDelegation tccDelegation = (TccDelegation) serviceTaskDelegation;


                try {
                    TccResult  tccResult = tccDelegation.tryExecute(executionContext);
                    if(tccResult != null){
                        if(tccResult.isSucessful()){
                            //do nothing
                        }else{

                            Object target = tccResult.getTarget();
                            Exception exception = new Exception(target.toString());
                            exceptionProcessor.process(exception);

                        }
                    }
                } catch (Exception e) {
                    exceptionProcessor.process(e);
                }

            }
        }


    }


}
