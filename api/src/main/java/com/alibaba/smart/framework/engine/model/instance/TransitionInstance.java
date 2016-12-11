package com.alibaba.smart.framework.engine.model.instance;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface TransitionInstance extends Instance {

    String getTransitionId();

    void setTransitionId(String transitionId);

    Long getSourceActivityInstanceId();

    void setSourceActivityInstanceId(Long sourceActivityInstanceId);

    Long getTargetActivityInstanceId();

    void setTargetActivityInstanceId(Long sourceActivityInstanceId);
}
