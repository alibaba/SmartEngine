package com.alibaba.smart.framework.engine.model.instance;

import com.alibaba.smart.framework.engine.param.Param;

/**
 * 持久化及序列化接口
 * Created by dongdongzdd on 16/8/11.
 */
public interface DatabaseMod<T, F extends Param> {

    String toDatabase();

    T getModel(F param);
}
