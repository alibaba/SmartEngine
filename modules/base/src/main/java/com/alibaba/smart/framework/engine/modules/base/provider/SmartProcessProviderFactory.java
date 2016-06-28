package com.alibaba.smart.framework.engine.modules.base.provider;

import com.alibaba.smart.framework.engine.modules.base.assembly.SmartProcess;
import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcess;

/**
 * Created by ettear on 16-4-14.
 */
public class SmartProcessProviderFactory implements ActivityProviderFactory<SmartProcess> {

    @Override
    public SmartProcessProvider createActivityProvider(RuntimeActivity activity) {
        return new SmartProcessProvider((RuntimeProcess) activity);
    }

    @Override
    public Class<SmartProcess> getModelType() {
        return SmartProcess.class;
    }
}
