package com.alibaba.smart.framework.engine.model.assembly;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface Activity extends Element {

    boolean isStartActivity();

    ExecutePolicy getExecutePolicy();

    void setExecutePolicy(ExecutePolicy executePolicy);

    boolean isSync();

    void setSync(boolean sync);

}
