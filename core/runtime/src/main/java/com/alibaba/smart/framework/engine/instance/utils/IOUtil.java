package com.alibaba.smart.framework.engine.instance.utils;

import java.io.Closeable;
import java.io.IOException;

public class IOUtil {

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }
}
