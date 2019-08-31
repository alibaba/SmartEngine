package com.alibaba.smart.framework.engine.test.mvel;

import java.io.Serializable;

import org.junit.Assert;
import org.junit.Test;
import org.mvel2.MVEL;
import org.mvel2.ast.ASTNode;
import org.mvel2.ast.BinaryOperation;
import org.mvel2.compiler.CompiledExpression;
import org.mvel2.compiler.ExecutableAccessor;

public class MvelTest {

    @Test
    public void test() throws Exception {
        String conditionText = "nrOfCompletedInstances/nrOfInstances >= 0.6 ";
        Serializable serializable = MVEL.compileExpression(conditionText);

        ExecutableAccessor executableAccessor = (ExecutableAccessor)serializable;
        BinaryOperation binaryOperation = (BinaryOperation)executableAccessor.getNode();
        ASTNode right = binaryOperation.getRight();
        Double rightValue = (Double)right.getLiteralValue();
        Assert.assertNotNull(serializable);
        Assert.assertEquals(0.6, rightValue.doubleValue(), 0.1);

    }

    @Test
    public void test1() throws Exception {
        String conditionText = "${nrOfCompletedInstances >= 1} ".trim();
        Serializable serializable = MVEL.compileExpression(conditionText);

        CompiledExpression executableAccessor = (CompiledExpression)serializable;
        Assert.assertNotNull(executableAccessor);
    }

    //@Test
    //public void test2() throws Exception {
    //
    //    String conditionText = "productId == 123 ".trim();
    //    Serializable serializable =  MVEL.compileExpression(conditionText);
    //    Map vars = new HashMap();
    //    Object o = MVEL.executeExpression(serializable, vars);
    //
    //    boolean b = (Boolean) o;
    //    Assert.assertFalse(b);
    //}
    //
    //@Test
    //public void test3() throws Exception {
    //    String conditionText = "empty  == productId ".trim();
    //    Serializable serializable =  MVEL.compileExpression(conditionText);
    //    Map vars = new HashMap();
    //    Object o = MVEL.executeExpression(serializable, vars);
    //
    //    boolean b = (Boolean) o;
    //    Assert.assertFalse(b);
    //}
}