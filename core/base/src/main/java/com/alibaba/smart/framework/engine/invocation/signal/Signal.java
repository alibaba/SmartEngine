package com.alibaba.smart.framework.engine.invocation.signal;

/**
 * 流程引擎信号,通过利用异常的灵活性随时做跳转使用
 * @author dongdong.zdd
 * @since 2016-12-13
 */
public abstract class Signal extends RuntimeException{

    public Signal() {
    }

    public Signal(String message) {
        super(message);
    }


    protected abstract String getSignal();


}
