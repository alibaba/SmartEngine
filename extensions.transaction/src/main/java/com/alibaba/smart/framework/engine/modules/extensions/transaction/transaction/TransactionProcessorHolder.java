package com.alibaba.smart.framework.engine.modules.extensions.transaction.transaction;

import org.springframework.context.ApplicationContext;

/**
 * @author Leo.yy   Created on 2017/12/6.
 * @description
 * @see
 */
public class TransactionProcessorHolder {

    private static TransactionProcessor processor;

    public static TransactionProcessor getProcessor() {
        return processor;
    }

    public static boolean isRegisterd() {
        return processor != null;
    }

    public static void register(TransactionProcessor processor) {
        TransactionProcessorHolder.processor = processor;
    }
}
