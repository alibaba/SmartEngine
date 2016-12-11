package com.alibaba.smart.framework.engine.persister.database.db.session;

public interface DeleteOperation {

    boolean isSameIdentityWith(PersistentObject other);

    void clearCache();

    void execute();

}
