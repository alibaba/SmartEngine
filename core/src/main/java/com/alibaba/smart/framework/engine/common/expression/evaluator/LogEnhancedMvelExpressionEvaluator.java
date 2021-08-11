package com.alibaba.smart.framework.engine.common.expression.evaluator;

import com.alibaba.smart.framework.engine.common.util.MvelUtil;
import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.configuration.impl.LogEnhancedParallelServiceOrchestration;
import org.mvel2.MVEL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  15:51.
 */
public class LogEnhancedMvelExpressionEvaluator implements ExpressionEvaluator {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogEnhancedMvelExpressionEvaluator.class);

    private final static int  defaultCacheSize                     = 128;

    /** 表达式缓存，key是表达式字符串，value是编译后的表达式。 */
    private final static ConcurrentHashMap<String, Serializable> expCache =
            new ConcurrentHashMap<String, Serializable>(defaultCacheSize);


    private static final String START_TAG = "${";
    private static final String END_TAG = "}";

    @Override
    public Object eval(String expression, Map<String, Object> vars,boolean needCached) {
        //编译表达式
        Serializable compiledExp = compileExp(expression,needCached);
        //执行表达式

        LOGGER.info("BEFORE: PvmActivityTask thread id  is {}, each param  is {} ,{} ",Thread.currentThread().getId(),expression,vars);

        Object result = MVEL.executeExpression(compiledExp, vars);

        LOGGER.info("AFTER: PvmActivityTask thread id  is {}, each param is {} , {} , {} ",Thread.currentThread().getId(),expression,vars,result);

        return result;

    }


    /**
     * 编译表达式。
     *
     * @param expression 表达式字符串
     * @return 编译后的表达式字符串
     */
    private static Serializable compileExp(String expression, boolean needCached) {
        String processedExp = expression.trim();

        // 兼容Activiti ${nrOfCompletedInstances >= 1} 这种 JUEL 表达式;通过下面的调用去掉首尾.

        if(processedExp.startsWith(START_TAG)){
            processedExp =  StringUtil.removeStart(processedExp, START_TAG);
            processedExp =  StringUtil.removeEnd(processedExp, END_TAG);
        }


        //首先从缓存里取，取不到则新编译。
        Serializable compiledExp = expCache.get(processedExp);

        if (null == compiledExp) {
            compiledExp = MVEL.compileExpression(processedExp);
            // cache 缓存结果

            if(needCached){
                expCache.put(processedExp, compiledExp);
            }
        }
        return compiledExp;
    }
}
