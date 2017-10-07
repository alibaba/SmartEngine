package com.alibaba.smart.framework.engine.instance.util;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import com.alibaba.smart.framework.engine.exception.EngineException;

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

    public static String readResourceFileAsUTF8String(String filePath) {
        File resourceFile = getResourceFile(filePath);
        byte[] buffer = new byte[(int) resourceFile.length()];
        BufferedInputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(resourceFile));
            inputStream.read(buffer);
        } catch(Exception e) {
            throw new EngineException("Couldn't read file " + filePath + ": " + e.getMessage());
        } finally {
            IOUtil.closeQuietly(inputStream);
        }
        return new String(buffer, Charset.forName("UTF-8"));
    }

    public static File getResourceFile(String filePath) {
        URL url = IOUtil.class.getClassLoader().getResource(filePath);
        try {
            return new File(url.toURI());
        } catch (Exception e) {
            throw new EngineException("Couldn't get file " + filePath + ": " + e.getMessage());
        }
    }

}
