package com.alibaba.smart.framework.engine.configuration;


import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

import java.util.concurrent.Callable;

public  interface PvmActivityTask extends Callable<ExecutionContext> {


}
