package com.alibaba.smart.framework.engine.configuration.scanner;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages extension bindings.
 */
public class ExtensionBindingManager {

    private final Map<Class<?>, Object> bindings = new HashMap<>();

    public void registerBinding(Class<?> clazz, Object instance) {
        bindings.put(clazz, instance);
    }

    public Object resolveBinding(Class<?> clazz) {
        return bindings.get(clazz);
    }

    public Map<Class<?>, Object> resolveBindingMap() {
        return bindings; // if you need direct access to the Map
    }
}

