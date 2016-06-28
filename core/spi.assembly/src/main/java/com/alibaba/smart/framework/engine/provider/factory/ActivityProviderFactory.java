package com.alibaba.smart.framework.engine.provider.factory;

import com.alibaba.smart.framework.engine.assembly.Activity;
import com.alibaba.smart.framework.engine.provider.ActivityProvider;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;

/**
 * Created by ettear on 16-4-11.
 */
public interface ActivityProviderFactory<M extends Activity> extends ProviderFactory<M> {

    ActivityProvider<M> createActivityProvider(RuntimeActivity activity);
}
