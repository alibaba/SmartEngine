package com.alibaba.smart.framework.engine.modules.extensions.transaction.transaction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Leo.yy   Created on 2017/8/3.
 * @description
 * @see
 */

public class LocalAsyncTransactionProcessor extends AbstractTransactionProcessor {

    private final static ExecutorService SERVICE = Executors.newFixedThreadPool(30);


    @Override
    public void saveForRollback(final TransactionProcessContext context) {
        SERVICE.submit(new Runnable() {
            @Override
            public void run() {
                LocalAsyncTransactionProcessor.this.rollback(context);
            }
        });
        logger.info("save task by async submit for rollback success" + context);
    }

    @Override
    public void saveForRedo(final TransactionProcessContext context) {
        SERVICE.submit(new Runnable() {
            @Override
            public void run() {
                LocalAsyncTransactionProcessor.this.redo(context);
            }
        });

        logger.info("save task by async submit for redo success" + context);

    }

}
