package com.alibaba.smart.framework.engine.test.storage.dual;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.extension.scanner.SimpleAnnotationScanner;
import com.alibaba.smart.framework.engine.storage.StorageRouter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Verify that the engine initializes successfully when both storage-mysql
 * and storage-custom modules are on the classpath.
 */
public class DualStorageEngineInitTest {

    protected SimpleAnnotationScanner annotationScanner;
    protected ProcessEngineConfiguration processEngineConfiguration;
    protected SmartEngine smartEngine;

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
    public void testEngineInitWithDualStorage() {
        assertNotNull("SmartEngine should be initialized", smartEngine);
        assertNotNull("ProcessEngineConfiguration should be set",
            smartEngine.getProcessEngineConfiguration());

        StorageRouter router = processEngineConfiguration.getStorageRouter();
        assertNotNull("StorageRouter should be initialized", router);
    }
}
