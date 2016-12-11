package com.alibaba.smart.framework.engine.persister.database.db.session;

public interface Session {

    void flush();

    void close();
}
