package com.alibaba.smart.framework.engine.test.storage.dual.helper;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;

import org.springframework.stereotype.Service;

@Service
public class AuditProcessServiceTaskDelegation implements TccDelegation {

    @Override
    public TccResult tryExecute(ExecutionContext executionContext) {
        return TccResult.buildSucessfulResult(executionContext.getRequest());
    }

    @Override
    public TccResult confirmExecute(ExecutionContext executionContext) {
        return null;
    }

    @Override
    public TccResult cancelExecute(ExecutionContext executionContext) {
        return null;
    }
}
