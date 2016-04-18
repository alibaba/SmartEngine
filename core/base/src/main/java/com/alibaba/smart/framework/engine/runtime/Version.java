package com.alibaba.smart.framework.engine.runtime;

import java.util.ResourceBundle;

/**
 * Created by ettear on 16-4-11.
 */
public class Version {
    public static final String VERSION;
    public static final String TIMESTAMP;
    static {
        ResourceBundle rb = ResourceBundle.getBundle("com/alibaba/smart/framework/engine/runtime/revision");
        VERSION = rb.getString("version");
        TIMESTAMP = rb.getString("timestamp");
    }

    public static String getVersion() {
        return VERSION;
    }

    public static String getTimestamp() {
        return TIMESTAMP;
    }
}
