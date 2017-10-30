package com.alibaba.smart.framework.engine.test.process;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.LockStrategy;
import com.alibaba.smart.framework.engine.configuration.MultiInstanceCounter;
import com.alibaba.smart.framework.engine.exception.LockException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.persister.database.dao.ProcessInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.util.SpringContextUtil;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.query.TaskQueryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 高海军 帝奇 74394 on 2017 January  18:03.
 */
@Service
public class DefaultLockStrategy implements LockStrategy {

    @Autowired
    private  SmartEngine smartEngine;

    @Override
    public void tryLock(Long processInstanceId) throws LockException {
        //ExtensionPointRegistry extensionPointRegistry = smartEngine.getProcessEngineConfiguration()
        //    .getExtensionPointRegistry();
        //PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        //
        //ProcessInstanceStorage processInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(ProcessInstanceStorage.class);
        //
        //processInstanceStorage.update()

        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)SpringContextUtil.getBean("processInstanceDAO");

        processInstanceDAO.tryLock(processInstanceId);

        //可以是设置 db uniqueKey 唯一索引； 或者在插入后直接再锁上； 或者使用其他中间件。
    }

    @Override
    public void unLock(Long processInstanceId) throws LockException {
        //do nothing
    }
}
