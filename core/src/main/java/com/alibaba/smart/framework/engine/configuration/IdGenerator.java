package com.alibaba.smart.framework.engine.configuration;

import java.util.concurrent.atomic.AtomicReference;

import com.alibaba.smart.framework.engine.model.instance.Instance;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  23:17.
 */
public interface IdGenerator {

    /**
     * Global ID type configuration holder.
     * Used by MyBatis TypeHandler to determine how to bind ID parameters.
     */
    AtomicReference<Class<?>> GLOBAL_ID_TYPE = new AtomicReference<>(Long.class);

    void generate(Instance instance);

    /**
     * Returns the Java type of generated ID.
     *
     * @return Long.class for numeric IDs, String.class for UUID/string IDs
     */
    default Class<?> getIdType() {
        return Long.class;
    }

    /**
     * Configure global ID type based on this generator's ID type.
     * Should be called during engine initialization.
     */
    default void configure() {
        GLOBAL_ID_TYPE.set(getIdType());
    }

    /**
     * Get the globally configured ID type.
     * Used by MyBatis TypeHandler.
     */
    static Class<?> getGlobalIdType() {
        return GLOBAL_ID_TYPE.get();
    }
}
