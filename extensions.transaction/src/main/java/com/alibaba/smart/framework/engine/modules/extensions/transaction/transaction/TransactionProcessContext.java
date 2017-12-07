package com.alibaba.smart.framework.engine.modules.extensions.transaction.transaction;


import com.alibaba.smart.framework.engine.modules.extensions.transaction.node.TransactionTask;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Leo.yy   Created on 2017/8/3.
 * @description
 * @see
 */
public class TransactionProcessContext implements Serializable {

    private static final long serialVersionUID = -1l;

    private TransactionTask transactionTask;

    private int errorChildIndex;

    private Exception exception;

    // 注意request里的对象必须可序列化
    private Map<String, Object> request;


    private int retryCount = 0;

    public final int MAX_RETRY_COUNT = 20;

    public Map<String, Object> getRequest() {
        return request;
    }

    public void setRequest(Map<String, Object> request) {
        this.request = request;
    }

    public TransactionTask getTransactionTask() {
        return transactionTask;
    }

    public void setTransactionTask(TransactionTask transactionTask) {
        this.transactionTask = transactionTask;
    }

    public int getErrorChildIndex() {
        return errorChildIndex;
    }

    public void setErrorChildIndex(int errorChildIndex) {
        this.errorChildIndex = errorChildIndex;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public void incrementRetryCount() {
        this.retryCount = (retryCount + 1);
    }

}

