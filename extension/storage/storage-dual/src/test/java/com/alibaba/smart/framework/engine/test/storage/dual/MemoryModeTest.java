package com.alibaba.smart.framework.engine.test.storage.dual;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.storage.StorageMode;
import com.alibaba.smart.framework.engine.storage.StorageModeHolder;
import com.alibaba.smart.framework.engine.storage.StorageRouter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Verify that when default mode is set to CUSTOM,
 * the scanner returns memory (custom) storage implementations.
 */
public class MemoryModeTest {

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
        StorageModeHolder.clearAll();
        if (processEngineConfiguration != null) {
            processEngineConfiguration.getAnnotationScanner().clear();
        }
    }

    @Test
    public void testMemoryModeViaDefaultSetting() {
        StorageRouter router = processEngineConfiguration.getStorageRouter();
        router.setDefaultMode(StorageMode.CUSTOM);

        assertEquals("Default mode should be CUSTOM", StorageMode.CUSTOM, router.getDefaultMode());

        // Verify router resolves to Custom (memory) implementation
        ProcessInstanceStorage storage = router.getStorage(ProcessInstanceStorage.class);
        assertNotNull("Storage should not be null", storage);
        assertTrue("Storage should be Custom (memory) implementation",
            storage.getClass().getSimpleName().startsWith("Custom"));

        // Also verify scanner returns a non-null proxy
        ProcessInstanceStorage proxyStorage = processEngineConfiguration.getAnnotationScanner()
            .getExtensionPoint(ExtensionConstant.COMMON, ProcessInstanceStorage.class);
        assertNotNull("Scanner should return a proxy", proxyStorage);
    }

    @Test
    public void testMemoryModeViaThreadLocal() {
        StorageModeHolder.set(StorageMode.CUSTOM);
        try {
            // Verify router resolves to Custom (memory) implementation
            StorageRouter router = processEngineConfiguration.getStorageRouter();
            ProcessInstanceStorage storage = router.getStorage(ProcessInstanceStorage.class);
            assertNotNull("Storage should not be null", storage);
            assertTrue("Storage should be Custom (memory) implementation when ThreadLocal is CUSTOM",
                storage.getClass().getSimpleName().startsWith("Custom"));
        } finally {
            StorageModeHolder.clear();
        }
    }
}
