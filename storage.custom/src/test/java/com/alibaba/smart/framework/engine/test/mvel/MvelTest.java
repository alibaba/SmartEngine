package com.alibaba.smart.framework.engine.test.mvel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.PersisterStrategy;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;
import com.alibaba.smart.framework.engine.test.AliPayIdGenerator;
import com.alibaba.smart.framework.engine.test.AliPayPersisterStrategy;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mvel2.MVEL;
import org.mvel2.ast.ASTNode;
import org.mvel2.ast.BinaryOperation;
import org.mvel2.compiler.ExecutableAccessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MvelTest {

    @Test
    public void test() throws Exception {
        String conditionText = "nrOfCompletedInstances/nrOfInstances >= 0.6 ";
        Serializable serializable =  MVEL.compileExpression(conditionText);

        ExecutableAccessor executableAccessor = (ExecutableAccessor)serializable;
        BinaryOperation binaryOperation = (BinaryOperation) executableAccessor.getNode();
        ASTNode right = binaryOperation.getRight();
        Double rightValue = (Double)right.getLiteralValue();
        Assert.assertNotNull(serializable);
        Assert.assertEquals(0.6,rightValue.doubleValue(),0.1);

    }


}