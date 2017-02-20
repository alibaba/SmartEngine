package com.alibaba.smart.framework.engine.common.expression.evaluator;

import org.mvel2.MVEL;
import org.mvel2.ParserContext;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  15:51.
 */
public class MvelExpressionEvaluator implements ExpressionEvaluator {

    /** 运算上下文，固定，无须每次都new一个。 */
   // private static ParserContext PARSER_CONTEXT                    = new ParserContext(null, null, null);
//    Caused by: java.lang.NullPointerException
//    at org.mvel2.ParserContext.initializeTables(ParserContext.java:458)
//   重要:在16个线程并发压测时,发现ParserContext.java:458 报了NPE。 怀疑asm 在对象初始化该类时存在线程安全性问题。 故此处不再只实例化一次PARSER_CONTEXT,

    /** 缓存默认初始大小以及最大值，当超过时会将缓存清空一次。一般一个流程里面尽量不要超出128个表达式 */
    private final static int  defaultCacheSize                     = 128;

    /** 表达式缓存，key是表达式字符串，value是编译后的表达式。 */
    private final ConcurrentHashMap<String, Serializable> expCache =
                                new ConcurrentHashMap<String, Serializable>(defaultCacheSize);

    @Override
    public boolean eval(String expression, Map<String, Object> vars) {
        //编译表达式
        Serializable compiledExp = compileExp(expression);
        //执行表达式
        Boolean result = (Boolean) MVEL.executeExpression(compiledExp, vars);

        return  result.booleanValue();
    }

    /**
     * 编译表达式。
     *
     * @param expression 表达式字符串
     * @return 编译后的表达式字符串
     */
    private Serializable compileExp(String expression) {
        String processedExp = expression.trim();

        //首先从缓存里取，取不到则新编译。
        Serializable compiledExp = expCache.get(processedExp);

        if (null == compiledExp) {
            compiledExp = MVEL.compileExpression(processedExp);
            // cache 缓存结果
            expCache.put(processedExp, compiledExp);
        }
        return compiledExp;
    }
}
