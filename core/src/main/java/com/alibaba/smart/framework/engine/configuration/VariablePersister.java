package com.alibaba.smart.framework.engine.configuration;

import java.util.Set;

/**
 * Created by 高海军 帝奇 74394 on 2017 October  06:20.
 */
public interface VariablePersister {

    /**
     * 是否开启变量存储机制,默认不开启.
     * @return
     */
    boolean isPersisteVariableInstanceEnabled();

    /**
     *黑名单中的 key 关联的数据将不会被持久化到变量表中.
     *
     * @return
     */
    Set<String> getBlockList();

    /**
     * 序列化.
     *
     * 除了基本类型和 String, 其他Value 的序列化机制.
     */
    String serialize(Object value);

    /**
     * 反序列化.
     *
     * 其他同  {@code serialize}
     *
     */
    <T> T deserialize(String text, Class<T> clazz);
}
