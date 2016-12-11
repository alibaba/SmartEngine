package com.alibaba.smart.framework.engine.persister.database.service;

import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.persister.database.dao.ActivityInstanceDAO;
import com.alibaba.smart.framework.engine.persister.util.SpringContextUtil;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存实例存储 Created by ettear on 16-4-13.
 */
public class RelationshipDatabaseActivityInstanceStorage implements ActivityInstanceStorage {



    @Override
    public ActivityInstance save(ActivityInstance instance) {
      Object s =  SpringContextUtil.getAppContext();
        return null;
    }

    @Override
    public ActivityInstance find(Long instanceId) {
        return null;
    }


    @Override
    public void remove(Long instanceId) {

    }
}
