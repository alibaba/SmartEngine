package com.alibaba.smart.framework.engine.modules.extensions.transaction.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Leo.yy   Created on 2017/8/9.
 * @description
 * @see
 */
public class SerializeUtils {

    public static byte[] serialize(Object obj) throws Exception {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.flush();
        return bos.toByteArray();

    }

    public static  <T> T deSerialize(byte[] data) throws Exception {

        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return (T)ois.readObject();
    }
}
