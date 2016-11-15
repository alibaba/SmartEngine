package com.alibaba.smart.framework.engine.context.factory;

import com.alibaba.smart.framework.engine.context.ExecutionContext;

/**
 * 实例上下文工厂 Created by ettear on 16-4-20.
 * TODO factory 太多
 * TODO 环节自循环
 * TODO 约定条件表达式里面的key
 * TODO 字符串 :协议:版本号 
 * TODO processStatus,资金/物流状态, 业务状态 会同时存在
 * TODO 每个子订单对应一个子流程实例,映射粒度细分 ,workflowengine 负责合并
 * TODO JAR 包依赖问题减少
 * TODO 子流程
 */
public interface InstanceContextFactory {

    ExecutionContext create();
}
