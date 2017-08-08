package com.alibaba.smart.framework.engine.provider;

import com.alibaba.smart.framework.engine.context.ExecutionContext;

/**
 * @author ettear
 * Created by ettear on 02/08/2017.
 */
public interface Invoker {


    /**
     * 执行一个方法
     * @param method 方法名
     * @param context 上下文
     * @return 结果
     */
    Object invoke(String method,ExecutionContext context);
}
