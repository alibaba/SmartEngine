package com.alibaba.smart.framework.engine.modules.extensions.transaction.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Leo.yy   Created on 2017/8/2.
 * @description
 * @see
 */
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;



    public static Object getBean(String beanName) {
        if (context == null) {
            return null;
        }
        return context.getBean(beanName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (context != null) {
            throw new RuntimeException("should not init more than once");
        }

        context = applicationContext;
    }
}
