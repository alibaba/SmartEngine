package com.alibaba.smart.framework.engine.modules.base.provider;

import com.alibaba.smart.framework.engine.modules.base.assembly.SmartActivity;
import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;

/**
 * The factory of smart activity
 * Created by ettear on 16-4-14.
 */
public class SmartActivityProviderFactory implements ActivityProviderFactory<SmartActivity> {

    @Override
    public SmartActivityProvider createActivityProvider(RuntimeActivity activity) {
        return new SmartActivityProvider(activity);
    }

    @Override
    public Class<SmartActivity> getModelType() {
        return SmartActivity.class;
    }
}
