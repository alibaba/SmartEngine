package com.alibaba.smart.framework.engine.provider.factory;

import com.alibaba.smart.framework.engine.model.assembly.ExecutePolicy;
import com.alibaba.smart.framework.engine.provider.ExecutePolicyBehavior;

/**
 * @author ettear
 * Created by ettear on 14/10/2017.
 */
public interface ExecutePolicyProviderFactory<M extends ExecutePolicy> extends ProviderFactory<M> {
    ExecutePolicyBehavior createExecutePolicyBehavior(M executePolicy);

}
