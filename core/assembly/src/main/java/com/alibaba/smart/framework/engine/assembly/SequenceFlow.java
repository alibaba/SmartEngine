package com.alibaba.smart.framework.engine.assembly;

/**
 * Created by ettear on 16-4-11.
 */
//TODO ettear 和 transition区别
public interface SequenceFlow extends Invocable {
    String getSourceRef();
    String getTargetRef();
}
