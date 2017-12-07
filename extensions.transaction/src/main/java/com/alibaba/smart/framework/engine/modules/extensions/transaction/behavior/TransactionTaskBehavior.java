package com.alibaba.smart.framework.engine.modules.extensions.transaction.behavior;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.action.ActionResult;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.action.SingleTaskAction;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.node.ErrorStrategy;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.node.SingleTask;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.node.TransactionTask;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.transaction.TransactionProcessContext;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.transaction.TransactionProcessor;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.transaction.TransactionProcessorHolder;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Leo.yy   Created on 2017/12/5.
 * @description
 * @see
 */
public class TransactionTaskBehavior extends AbstractActivityBehavior<TransactionTask> implements ActivityBehavior {

    private final static Logger logger = LoggerFactory.getLogger(TransactionTaskBehavior.class);

    public TransactionTaskBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity pvmActivity) {
        super(extensionPointRegistry, pvmActivity);
    }

    @Override
    public void leave(ExecutionContext executionContext) {

        ProcessInstance processInstance = executionContext.getProcessInstance();
        ActivityInstance activityInstance = super.activityInstanceFactory.create(getModel(), executionContext);

        ExecutionInstance executionInstance = super.executionInstanceFactory.create(activityInstance, executionContext);

        List<ExecutionInstance> executionInstanceList = new ArrayList<ExecutionInstance>();
        executionInstanceList.add(executionInstance);
        activityInstance.setExecutionInstanceList(executionInstanceList);
        processInstance.addActivityInstance(activityInstance);

        TransactionTask task = getModel();
        List<SingleTask> childTasks = task.getChildTasks();

        Map<String, Object> request = executionContext.getRequest();

        out:
        for (int i = 0; i < childTasks.size(); i++) {
            SingleTask childTask = childTasks.get(i);

            try {
                SingleTaskAction singleTaskAction = childTask.getAction();
                if (singleTaskAction == null) {
                    continue;
                }

                String id = childTask.getId();

                logger.info("execute action:" + singleTaskAction.getClass().getName());
                ActionResult result = singleTaskAction.invoke(request, childTask);

                if (result == null) {
                    throw new RuntimeException(String.format("task %s return null result!", id));
                }


                if (!result.isSuccess()) {
                    throw new RuntimeException("run task fail :"+result.getEroMsg());
                }

                // need to break current process
                if (result.isBreakCurrentProcess()) {
                    break out;
                }


            } catch (Exception e) {

                logger.error("execute transaction task fail: ", e);

                TransactionProcessor processor = TransactionProcessorHolder.getProcessor();
                if (processor == null) {
                    logger.error("transaction processor null, ignore any rollback or redo strategy");
                    break out;
                }

                TransactionProcessContext context = new TransactionProcessContext();
                context.setErrorChildIndex(i);
                context.setException(e);
                context.setRequest(request);
                context.setTransactionTask(task);

                ErrorStrategy strategy = null;
                ErrorStrategy curStrategy = childTask.getErrorStrategy();
                if (curStrategy != null) {
                    strategy = curStrategy;
                } else {
                    strategy = task.getErrorStrategy();
                }

                if (strategy == null) {
                    strategy = ErrorStrategy.EMPTY;
                }

                switch (strategy) {
                    case ROLLBACK:
                        processor.saveForRollback(context);
                        break out;

                    case REDO:

                        processor.saveForRedo(context);

                        break out;
                    case EMPTY:

                        logger.error("execute action fail , action index:"+i+", error strategy is empty, so do nothing about it");
                        // nothing
                        break out;
                }
            }
        }

    }


}
