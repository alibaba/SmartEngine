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
 * Verify that the default mode is DATABASE and the scanner returns
 * database storage implementations.
 */
public class DefaultDatabaseModeTest {

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
    public void testDefaultModeIsDatabase() {
        StorageRouter router = processEngineConfiguration.getStorageRouter();
        assertEquals("Default mode should be DATABASE", StorageMode.DATABASE, router.getDefaultMode());
        assertEquals("Resolved mode should be DATABASE when no ThreadLocal set",
            StorageMode.DATABASE, router.resolveCurrentMode());
    }

    @Test
    public void testScannerReturnsDatabaseStorageByDefault() {
        // When no ThreadLocal is set, getExtensionPoint should return DATABASE storage
        ProcessInstanceStorage storage = processEngineConfiguration.getAnnotationScanner()
            .getExtensionPoint(ExtensionConstant.COMMON, ProcessInstanceStorage.class);

        assertNotNull("Storage should not be null", storage);
        assertTrue("Default storage should be database implementation (not Custom)",
            !storage.getClass().getSimpleName().startsWith("Custom"));
    }
}
