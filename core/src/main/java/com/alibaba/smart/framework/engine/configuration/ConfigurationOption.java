package com.alibaba.smart.framework.engine.configuration;

import com.alibaba.smart.framework.engine.configuration.impl.option.DisabledOption;
import com.alibaba.smart.framework.engine.configuration.impl.option.ExpressionCompileResultCachedOption;
import com.alibaba.smart.framework.engine.configuration.impl.option.ServiceOrchestrationEnabledOption;
import com.alibaba.smart.framework.engine.configuration.impl.option.TransferEnabledOption;

/**
 * Created by 高海军 帝奇 74394 on  2020-02-15 21:40.
 */
public interface ConfigurationOption {

    ConfigurationOption DISABLED_OPTION = new DisabledOption();


    ConfigurationOption TRANSFER_ENABLED_OPTION = new TransferEnabledOption();

    ConfigurationOption EXPRESSION_COMPILE_RESULT_CACHED_OPTION = new ExpressionCompileResultCachedOption();

    ConfigurationOption SERVICE_ORCHESTRATION_OPTION = new ServiceOrchestrationEnabledOption(  );

    boolean isEnabled();

    String getId();

    Object getData();

}