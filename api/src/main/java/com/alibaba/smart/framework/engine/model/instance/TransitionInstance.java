package com.alibaba.smart.framework.engine.model.instance;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface TransitionInstance extends Instance {

    String getTransitionId();

    void setTransitionId(String transitionId);

    String getSourceActivityInstanceId();

    void setSourceActivityInstanceId(String sourceActivityInstanceId);

    String getTargetActivityInstanceId();

    void setTargetActivityInstanceId(String sourceActivityInstanceId);
}
