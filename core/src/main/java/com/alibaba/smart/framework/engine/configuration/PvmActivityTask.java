package com.alibaba.smart.framework.engine.configuration;

import com.alibaba.smart.framework.engine.context.ExecutionContext;

import java.util.concurrent.Callable;

public interface PvmActivityTask extends Callable<ExecutionContext> {}
