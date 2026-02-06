package com.alibaba.smart.framework.engine.test.storage.dual;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.storage.StorageMode;
import com.alibaba.smart.framework.engine.storage.StorageModeHolder;
import com.alibaba.smart.framework.engine.storage.StorageRouter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Verify dynamic mode switching via StorageModeHolder ThreadLocal.
 */
public class ThreadLocalModeSwitchTest {

    private ProcessEngineConfiguration processEngineConfiguration;
    private SmartEngine smartEngine;
    private StorageRouter router;

    @Before
    public void setUp() {
        processEngineConfiguration = new DefaultProcessEngineConfiguration();
        smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);
        router = processEngineConfiguration.getStorageRouter();
    }

    @After
    public void tearDown() {
        StorageModeHolder.clearAll();
        if (processEngineConfiguration != null) {
            processEngineConfiguration.getAnnotationScanner().clear();
        }
    }

    @Test
    public void testSwitchBetweenModes() {
        // Default: DATABASE mode — router resolves to database storage
        ProcessInstanceStorage dbStorage = router.getStorage(ProcessInstanceStorage.class);
        assertNotNull(dbStorage);
        assertTrue("Default should be database storage",
            !dbStorage.getClass().getSimpleName().startsWith("Custom"));

        // Switch to CUSTOM mode — router resolves to custom storage
        StorageModeHolder.set(StorageMode.CUSTOM);
        try {
            ProcessInstanceStorage memStorage = router.getStorage(ProcessInstanceStorage.class);
            assertNotNull(memStorage);
            assertTrue("Should be memory (Custom) storage after ThreadLocal switch",
                memStorage.getClass().getSimpleName().startsWith("Custom"));
        } finally {
            StorageModeHolder.clear();
        }

        // Back to DATABASE mode after clearing ThreadLocal
        ProcessInstanceStorage dbStorageAgain = router.getStorage(ProcessInstanceStorage.class);
        assertNotNull(dbStorageAgain);
        assertTrue("Should return to database storage after clearing ThreadLocal",
            !dbStorageAgain.getClass().getSimpleName().startsWith("Custom"));
    }

    @Test
    public void testThreadLocalOverridesDefault() {
        // Set default to CUSTOM
        router.setDefaultMode(StorageMode.CUSTOM);

        // ThreadLocal overrides to DATABASE
        StorageModeHolder.set(StorageMode.DATABASE);
        try {
            ProcessInstanceStorage storage = router.getStorage(ProcessInstanceStorage.class);
            assertNotNull(storage);
            assertTrue("ThreadLocal DATABASE should override default CUSTOM",
                !storage.getClass().getSimpleName().startsWith("Custom"));
        } finally {
            StorageModeHolder.clear();
        }
    }
}
