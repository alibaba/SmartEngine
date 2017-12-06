package com.alibaba.smart.framework.engine.modules.extensions.transaction.transaction;

/**
 * @author Leo.yy   Created on 2017/8/3.
 * @description
 * @see
 */
public interface TransactionProcessor {

    void saveForRollback(TransactionProcessContext context);

    void saveForRedo(TransactionProcessContext context);


    boolean rollback(TransactionProcessContext context);

    boolean redo(TransactionProcessContext context);

}
