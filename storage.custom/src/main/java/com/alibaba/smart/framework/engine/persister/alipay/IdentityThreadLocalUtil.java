package com.alibaba.smart.framework.engine.persister.alipay;

import java.io.Serializable;

public class IdentityThreadLocalUtil {

    private static Serializable identity ;

    public static void set(Serializable value) {
            identity = value;
    }

    public static void remove() {
        identity = null;
    }

    public static Serializable get() {
        return identity;
    }

}
