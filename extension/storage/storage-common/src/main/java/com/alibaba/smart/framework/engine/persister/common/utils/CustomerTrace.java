package com.alibaba.smart.framework.engine.persister.common.utils;

import java.util.HashMap;

/**
 * 基于线程上下文操作属性（企业id，或客户id，或租户id）工具类
 * yrc on 2025/5/11.
 */
public class CustomerTrace {
    public static final String CUSTOMER_ID = "_$_smart_engine_$_customer_id";
    private static final ThreadLocal<CustomerTrace> threadLocal = new InheritableThreadLocal<>();
    private final HashMap<String, String> properties = new HashMap<>();

    public static void putCustomerId(String value) {
        CustomerTrace customerTrace = threadLocal.get();
        if (customerTrace == null) {
            customerTrace = new CustomerTrace();
            threadLocal.set(customerTrace);
        }
        customerTrace.properties.put(CUSTOMER_ID, value);
    }

    public static void removeCustomerId() {
        CustomerTrace customerTrace = threadLocal.get();
        if (customerTrace != null) {
            customerTrace.properties.remove(CUSTOMER_ID);
        }
    }

    public static String getCustomerId() {
        CustomerTrace customerTrace = threadLocal.get();
        if (customerTrace != null) {
            return customerTrace.properties.get(CUSTOMER_ID);
        }

        return "-1";
    }

    public static void putProperty(String key, String value) {
        CustomerTrace customerTrace = threadLocal.get();
        if (customerTrace == null) {
            customerTrace = new CustomerTrace();
            threadLocal.set(customerTrace);
        }
        customerTrace.properties.put(key, value);
    }

    public static String getProperty(String key) {
        CustomerTrace customerTrace = threadLocal.get();
        if (customerTrace != null) {
            return customerTrace.properties.get(key);
        }
        return null;
    }

    public static void removeProperty(String key) {
        CustomerTrace customerTrace = threadLocal.get();
        if (customerTrace != null) {
            customerTrace.properties.remove(key);
        }
    }

    public static void removeProperties() {
        CustomerTrace customerTrace = threadLocal.get();
        if (customerTrace != null) {
            customerTrace.properties.clear();
        }
    }


    public static HashMap<String, String> getProperties() {
        CustomerTrace customerTrace = threadLocal.get();
        if (customerTrace != null) {
            return customerTrace.properties;
        } else {
            return null;
        }
    }

    public static boolean propertyExists(String key) {
        CustomerTrace customerTrace = threadLocal.get();
        if (customerTrace != null) {
            return customerTrace.properties.containsKey(key);
        } else {
            return false;
        }
    }

    public static int size() {
        CustomerTrace customerTrace = threadLocal.get();
        if (customerTrace != null) {
            return customerTrace.properties.size();
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "customerTrace{" +
                "map=" + properties +
                '}';
    }
}
