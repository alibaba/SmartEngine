package com.alibaba.smart.framework.engine.common.expression.evaluator;

import org.mvel2.MVEL;
import org.mvel2.ast.ASTNode;
import org.mvel2.ast.BinaryOperation;
import org.mvel2.compiler.ExecutableAccessor;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.smart.framework.engine.common.util.StringUtil;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  15:51.
 */
public class MvelExpressionEvaluator implements ExpressionEvaluator {


    private final static int  defaultCacheSize                     = 128;

    /** 表达式缓存，key是表达式字符串，value是编译后的表达式。 */
    private final static ConcurrentHashMap<String, Serializable> expCache =
                                new ConcurrentHashMap<String, Serializable>(defaultCacheSize);

    @Override
    public Object eval(String expression, Map<String, Object> vars) {
        //编译表达式
        Serializable compiledExp = compileExp(expression);
        //执行表达式
        return MVEL.executeExpression(compiledExp, vars);
    }


    //public static boolean staticEval(String expression, Map<String, Object> vars) {
    //    //编译表达式
    //    Serializable compiledExp = compileExp(expression);
    //    //执行表达式
    //    Boolean result = (Boolean) MVEL.executeExpression(compiledExp, vars);
    //
    //    return  result.booleanValue();
    //}

    /**
     * 编译表达式。
     *
     * @param expression 表达式字符串
     * @return 编译后的表达式字符串
     */
    private static Serializable compileExp(String expression) {
        String processedExp = expression.trim();

        // 兼容Activiti ${nrOfCompletedInstances >= 1} 这种 JUEL 表达式;通过下面的调用去掉首尾.
        processedExp =  StringUtil.removeStart(processedExp.trim(),"${");
        processedExp =  StringUtil.removeEnd(processedExp.trim(),"}");


        //首先从缓存里取，取不到则新编译。
        Serializable compiledExp = expCache.get(processedExp);

        if (null == compiledExp) {
            compiledExp = MVEL.compileExpression(processedExp);
            // cache 缓存结果
            expCache.put(processedExp, compiledExp);
        }
        return compiledExp;
    }

    public static Number getRightValueForBinaryOperationExpression(String expression) {
        Serializable serializable= compileExp(expression);
        ExecutableAccessor executableAccessor = (ExecutableAccessor)serializable;
        BinaryOperation binaryOperation = (BinaryOperation) executableAccessor.getNode();
        ASTNode right = binaryOperation.getRight();
        Number rightValue = (Number)right.getLiteralValue();
        return rightValue;
    }
}
