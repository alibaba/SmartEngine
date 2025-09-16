package com.alibaba.smart.framework.engine.common.util;

import org.mvel2.MVEL;
import org.mvel2.ast.ASTNode;
import org.mvel2.ast.BinaryOperation;
import org.mvel2.compiler.ExecutableAccessor;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Created by 高海军 帝奇 74394 on 2019-11-12 15:53. */
public abstract class MvelUtil {
    private static final int defaultCacheSize = 128;

    /** 表达式缓存，key是表达式字符串，value是编译后的表达式。 */
    private static final ConcurrentHashMap<String, Serializable> expCache =
            new ConcurrentHashMap<String, Serializable>(defaultCacheSize);

    private static final String START_TAG = "${";
    private static final String END_TAG = "}";

    public static Object eval(String expression, Map<String, Object> vars, boolean needCached) {
        // 编译表达式
        Serializable compiledExp = compileExp(expression, needCached);
        // 执行表达式
        return MVEL.executeExpression(compiledExp, vars);
    }

    // public static boolean staticEval(String expression, Map<String, Object> vars) {
    //    //编译表达式
    //    Serializable compiledExp = compileExp(expression);
    //    //执行表达式
    //    Boolean result = (Boolean) MVEL.executeExpression(compiledExp, vars);
    //
    //    return  result.booleanValue();
    // }

    /**
     * 编译表达式。
     *
     * @param expression 表达式字符串
     * @return 编译后的表达式字符串
     */
    private static Serializable compileExp(String expression, boolean needCached) {
        String processedExp = expression.trim();

        // 兼容Activiti ${nrOfCompletedInstances >= 1} 这种 JUEL 表达式;通过下面的调用去掉首尾.

        if (processedExp.startsWith(START_TAG)) {
            processedExp = StringUtil.removeStart(processedExp, START_TAG);
            processedExp = StringUtil.removeEnd(processedExp, END_TAG);
        }

        // 首先从缓存里取，取不到则新编译。
        Serializable compiledExp = expCache.get(processedExp);

        if (null == compiledExp) {
            compiledExp = MVEL.compileExpression(processedExp);
            // cache 缓存结果

            if (needCached) {
                expCache.put(processedExp, compiledExp);
            }
        }
        return compiledExp;
    }

    public static Number getRightValueForBinaryOperationExpression(String expression) {
        Serializable serializable = compileExp(expression, true);
        ExecutableAccessor executableAccessor = (ExecutableAccessor) serializable;
        BinaryOperation binaryOperation = (BinaryOperation) executableAccessor.getNode();
        ASTNode right = binaryOperation.getRight();
        Number rightValue = (Number) right.getLiteralValue();
        return rightValue;
    }
}
