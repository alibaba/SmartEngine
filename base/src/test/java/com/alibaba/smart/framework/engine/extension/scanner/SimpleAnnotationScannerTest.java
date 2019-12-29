package com.alibaba.smart.framework.engine.extension.scanner;

import java.util.Map;

import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by 高海军 帝奇 74394 on  2019-08-25 22:04.
 */
public class SimpleAnnotationScannerTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void scan() {
       new SimpleAnnotationScanner().scan("com.alibaba.smart.framework.engine.extension.scanner",
            ExtensionBinding.class);

        Map<String,ExtensionBindingResult> resultClass =SimpleAnnotationScanner.getMap();
        Assert.assertEquals(2,resultClass.size());
        ExtensionBindingResult parser = resultClass.get("ELEMENT_PARSER");
        Assert.assertEquals(2, parser.getBindings().entrySet().size());

        parser = resultClass.get("Behavior");
        Assert.assertEquals(2, parser.getBindings().entrySet().size());

    }
}