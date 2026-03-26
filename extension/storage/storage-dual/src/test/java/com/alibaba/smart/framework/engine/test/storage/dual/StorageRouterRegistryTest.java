package com.alibaba.smart.framework.engine.test.storage.dual;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskAssigneeStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.VariableInstanceStorage;
import com.alibaba.smart.framework.engine.storage.StorageMode;
import com.alibaba.smart.framework.engine.storage.StorageRouter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Verify that StorageRouter has registered both DATABASE and CUSTOM mode
 * storage implementations for all 6 storage types.
 */
public class StorageRouterRegistryTest {

    private ProcessEngineConfiguration processEngineConfiguration;
    private SmartEngine smartEngine;

    @Before
    public void setUp() {
        processEngineConfiguration = new DefaultProcessEngineConfiguration();
        smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);
    }

    @After
    public void tearDown() {
        if (processEngineConfiguration != null) {
            processEngineConfiguration.getAnnotationScanner().clear();
        }
    }

    @Test
    public void testDatabaseModeRegistered() {
        StorageRouter router = processEngineConfiguration.getStorageRouter();
        assertNotNull(router);

        assertNotNull("DATABASE ProcessInstanceStorage should be registered",
            router.getStorage(ProcessInstanceStorage.class, StorageMode.DATABASE));
        assertNotNull("DATABASE ExecutionInstanceStorage should be registered",
            router.getStorage(ExecutionInstanceStorage.class, StorageMode.DATABASE));
        assertNotNull("DATABASE ActivityInstanceStorage should be registered",
            router.getStorage(ActivityInstanceStorage.class, StorageMode.DATABASE));
        assertNotNull("DATABASE TaskInstanceStorage should be registered",
            router.getStorage(TaskInstanceStorage.class, StorageMode.DATABASE));
        assertNotNull("DATABASE TaskAssigneeStorage should be registered",
            router.getStorage(TaskAssigneeStorage.class, StorageMode.DATABASE));
        assertNotNull("DATABASE VariableInstanceStorage should be registered",
            router.getStorage(VariableInstanceStorage.class, StorageMode.DATABASE));
    }

    @Test
    public void testCustomModeRegistered() {
        StorageRouter router = processEngineConfiguration.getStorageRouter();
        assertNotNull(router);

        assertNotNull("CUSTOMProcessInstanceStorage should be registered",
            router.getStorage(ProcessInstanceStorage.class, StorageMode.CUSTOM));
        assertNotNull("CUSTOMExecutionInstanceStorage should be registered",
            router.getStorage(ExecutionInstanceStorage.class, StorageMode.CUSTOM));
        assertNotNull("CUSTOMActivityInstanceStorage should be registered",
            router.getStorage(ActivityInstanceStorage.class, StorageMode.CUSTOM));
        assertNotNull("CUSTOMTaskInstanceStorage should be registered",
            router.getStorage(TaskInstanceStorage.class, StorageMode.CUSTOM));
        assertNotNull("CUSTOMTaskAssigneeStorage should be registered",
            router.getStorage(TaskAssigneeStorage.class, StorageMode.CUSTOM));
        assertNotNull("CUSTOMVariableInstanceStorage should be registered",
            router.getStorage(VariableInstanceStorage.class, StorageMode.CUSTOM));
    }

    @Test
    public void testDatabaseAndCustomAreDifferentInstances() {
        StorageRouter router = processEngineConfiguration.getStorageRouter();

        Object dbStorage = router.getStorage(ProcessInstanceStorage.class, StorageMode.DATABASE);
        Object memStorage = router.getStorage(ProcessInstanceStorage.class, StorageMode.CUSTOM);

        assertTrue("DATABASE and CUSTOM ProcessInstanceStorage should be different instances",
            dbStorage != memStorage);
        assertTrue("DATABASE storage should not be Custom implementation",
            !dbStorage.getClass().getSimpleName().startsWith("Custom"));
        assertTrue("CUSTOMstorage should be Custom implementation",
            memStorage.getClass().getSimpleName().startsWith("Custom"));
    }
}
