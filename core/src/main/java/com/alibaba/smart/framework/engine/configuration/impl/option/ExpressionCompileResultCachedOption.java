package com.alibaba.smart.framework.engine.configuration.impl.option;

import com.alibaba.smart.framework.engine.configuration.ConfigurationOption;

/**
 * Created by 高海军 帝奇 74394 on  2020-02-15 21:40.

 * 目前仅用于cache 表达式的解析结果。一般来说，是需要cache的。但是如果表达式字面内容完全相同，但是变量的类型的不同，会导致运行时错误，这种情况下则需要设置为false。
 */
public class ExpressionCompileResultCachedOption implements ConfigurationOption {

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getId() {
        return "expressionCompileResultCached";
    }

    @Override
    public Object getData() {
        return null;
    }
}