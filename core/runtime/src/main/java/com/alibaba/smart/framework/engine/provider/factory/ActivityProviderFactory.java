package com.alibaba.smart.framework.engine.provider.factory;

import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.provider.ActivityBehaviorProvider;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

/**
 * Created by ettear on 16-4-11.
 */
public interface ActivityProviderFactory<M extends Activity> extends ProviderFactory<M> {

    ActivityBehaviorProvider<M> createActivityProvider(PvmActivity activity);
}
