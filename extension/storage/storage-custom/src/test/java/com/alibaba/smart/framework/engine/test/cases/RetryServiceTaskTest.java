package com.alibaba.smart.framework.engine.test.cases;

import com.alibaba.smart.framework.engine.annoation.Retryable;
import com.alibaba.smart.framework.engine.configuration.ExceptionProcessor;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultDelegationExecutor;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.util.ThreadPoolUtil;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public class RetryServiceTaskTest extends CustomBaseTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetryServiceTaskTest.class);


    protected void initProcessConfiguration() {
        super.initProcessConfiguration();

        RetryableDelegationExecutor delegationExecutor = new RetryableDelegationExecutor();
        processEngineConfiguration.setDelegationExecutor(delegationExecutor);
        processEngineConfiguration.setExceptionProcessor(null);
    }

    class RetryableDelegationExecutor extends DefaultDelegationExecutor{
        protected void execute(ExecutionContext context, JavaDelegation delegation, ExceptionProcessor exceptionProcessor , boolean present) {


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

                if(attemptCount == maxAttempts ){
                    // means all retry failed
                    // record log ,trigger alert, persist params if needed

                    LOGGER.warn("all failed");
                }
            }
        }

        protected   void dealException(ExceptionProcessor exceptionProcessor, Exception exception,ExecutionContext context) {


        }

    }

    @Test
    public void test() throws Exception {

        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("all-retry-servicetask.bpmn.xml").getFirstProcessDefinition();
        assertEquals(5, processDefinition.getBaseElementList().size());

        ProcessInstance processInstance =  processCommandService.start(processDefinition.getId(),processDefinition.getVersion());

        Assert.assertEquals(InstanceStatus.completed,processInstance.getStatus());
    }

}