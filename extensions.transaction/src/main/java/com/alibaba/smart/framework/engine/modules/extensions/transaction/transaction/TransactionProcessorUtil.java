package com.alibaba.smart.framework.engine.modules.extensions.transaction.transaction;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * @author Leo.yy   Created on 2017/8/3.
 * @description
 * @see
 */
public class TransactionProcessorUtil implements ApplicationContextAware, InitializingBean {

    private static ApplicationContext context;

    private static TransactionProcessor processor;

    public static TransactionProcessor getProcessor() {
        return processor;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        TransactionProcessorUtil.context = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, TransactionProcessor> processors = context.getBeansOfType(TransactionProcessor.class);

        if (processors == null || processors.size() == 0) {
            throw new RuntimeException("TransactionProcessor未注册");
        }

        if (processors.size() > 1) {
            throw new RuntimeException("超过1个TransactionProcessor未注册被注册");
        }

        TransactionProcessorUtil.processor = processors.values().iterator().next();

    }
}
