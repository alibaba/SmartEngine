package com.alibaba.smart.framework.engine.instance;

/**
 * Created by ettear on 16-4-19.
 */
public interface TransitionInstance extends Instance {

    String getTransitionId();

    void setTransitionId(String transitionId);

    String getSourceActivityInstanceId();

    void setSourceActivityInstanceId(String sourceActivityInstanceId);

    String getTargetActivityInstanceId();

    void setTargetActivityInstanceId(String sourceActivityInstanceId);
}
