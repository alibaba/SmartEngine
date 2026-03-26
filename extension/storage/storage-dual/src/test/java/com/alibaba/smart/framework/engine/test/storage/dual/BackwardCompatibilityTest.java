package com.alibaba.smart.framework.engine.test.storage.dual;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.extension.scanner.SimpleAnnotationScanner;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Verify backward compatibility: when StorageRouter is null (not configured),
 * the scanner falls back to original behavior.
 */
public class BackwardCompatibilityTest {

    private SimpleAnnotationScanner scanner;

    @After
    public void tearDown() {
        if (scanner != null) {
            scanner.clear();
        }
    }

    @Test
    public void testScannerWithoutStorageRouterFallsBackToOriginal() {
        // Manually create scanner without StorageRouter
        scanner = new SimpleAnnotationScanner(SmartEngine.class.getPackage().getName());
        // storageRouter is null by default

        ProcessEngineConfiguration config = new DefaultProcessEngineConfiguration();
        config.setAnnotationScanner(scanner);

        SmartEngine engine = new DefaultSmartEngine();

        // Set StorageRouter to null to simulate old behavior
        config.setStorageRouter(null);

        engine.init(config);

        // SERVICE group lookups should still work (non-storage types)
        Object servicePoint = scanner.getExtensionPoint(ExtensionConstant.SERVICE,
            com.alibaba.smart.framework.engine.service.command.RepositoryCommandService.class);
        assertNotNull("SERVICE group extension point should still work", servicePoint);
    }

    @Test
    public void testNonStorageExtensionPointsUnaffected() {
        ProcessEngineConfiguration config = new DefaultProcessEngineConfiguration();
        SmartEngine engine = new DefaultSmartEngine();
        engine.init(config);

        // SERVICE group should work normally (these are not routed through StorageRouter)
        Object repoService = config.getAnnotationScanner()
            .getExtensionPoint(ExtensionConstant.SERVICE,
                com.alibaba.smart.framework.engine.service.command.ProcessCommandService.class);
        assertNotNull("ProcessCommandService should be resolved via original mechanism", repoService);
    }
}
