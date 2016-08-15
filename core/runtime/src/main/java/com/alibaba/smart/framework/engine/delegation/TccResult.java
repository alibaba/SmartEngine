package com.alibaba.smart.framework.engine.delegation;

public interface TccResult<T> {

    boolean isSuccessful();

    void setSuccessful(boolean isSuccessful);

    void setTarget(T target);

    T getTarget();

}
