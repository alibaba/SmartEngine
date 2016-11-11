package com.alibaba.smart.framework.engine.delegation;
/**
 * @author 高海军 帝奇  2016.11.11
 */
public interface TccResult<T> {

    boolean isSuccessful();

    void setSuccessful(boolean isSuccessful);

    void setTarget(T target);

    T getTarget();

}
