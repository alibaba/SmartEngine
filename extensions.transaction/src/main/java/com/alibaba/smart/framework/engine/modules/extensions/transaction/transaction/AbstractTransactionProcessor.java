package com.alibaba.smart.framework.engine.modules.extensions.transaction.transaction;

import com.alibaba.smart.framework.engine.modules.extensions.transaction.action.ActionResult;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.action.SingleTaskAction;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.node.SingleTask;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.node.TransactionTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author Leo.yy   Created on 2017/8/3.
 * @description
 * @see
 */
public abstract class AbstractTransactionProcessor implements TransactionProcessor {

    protected final static Logger logger = LoggerFactory.getLogger(AbstractTransactionProcessor.class);


    @Override
    public boolean rollback(TransactionProcessContext context) {


        TransactionTask task = context.getTransactionTask();
        Map<String, Object> request = context.getRequest();
        List<SingleTask> childTasks = task.getChildTasks();


        int failChildIndex = context.getErrorChildIndex();

        boolean allSuccess = true;
        for (int i = 0; i <= failChildIndex; i++) {
            SingleTask childTask = childTasks.get(i);
            SingleTaskAction action = childTask.getAction();
            ActionResult result = action.rollback(request, childTask);
            if (result == null || !result.isSuccess()) {
                allSuccess = false;
            }
        }

        return allSuccess;
    }

    @Override
    public boolean redo(TransactionProcessContext context) {

        TransactionTask task = context.getTransactionTask();
        Map<String, Object> request = context.getRequest();
        List<SingleTask> childTasks = task.getChildTasks();

        int failChildIndex = context.getErrorChildIndex();

        boolean allSuccess = true;
        for (int i = failChildIndex; i < childTasks.size(); i++) {
            SingleTask childTask = childTasks.get(i);
            SingleTaskAction action = childTask.getAction();
            ActionResult result = action.invoke(request, childTask);
            if (result == null || !result.isSuccess()) {
                allSuccess = false;
            }
        }

        return allSuccess;
    }

}
