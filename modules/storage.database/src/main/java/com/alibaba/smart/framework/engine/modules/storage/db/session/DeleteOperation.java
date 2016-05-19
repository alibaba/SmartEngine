package com.alibaba.smart.framework.engine.modules.storage.db.session;
public interface DeleteOperation {

    boolean sameIdentity(PersistentObject other);

    void clearCache();

    void execute();

}