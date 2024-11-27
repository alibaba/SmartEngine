package com.alibaba.smart.framework.engine.configuration.scanner;

import java.util.HashMap;
import java.util.Map;

/**
 * Refactored version of ExtensionBindingResult to encapsulate binding map operations.
 */
public class ExtensionBindingResult {

    private final Map<Class<?>, Object> bindingMap = new HashMap<>();

    // Adds a new binding
    public void addBinding(Class<?> key, Object value) {
        bindingMap.put(key, value);
    }

    // Retrieves a binding
    public Object getBinding(Class<?> key) {
        return bindingMap.get(key);
    }

    // Checks if a binding exists
    public boolean hasBinding(Class<?> key) {
        return bindingMap.containsKey(key);
    }

    // Exposes the map if absolutely necessary
    public Map<Class<?>, Object> getBindingMap() {
        return bindingMap;
    }

    public void setBindingMap(Object value) {
        Class<?> key = null;
        bindingMap.put(key, value);

    }
}
