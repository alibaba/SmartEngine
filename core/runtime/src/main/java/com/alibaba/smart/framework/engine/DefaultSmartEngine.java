package com.alibaba.smart.framework.engine;

import com.alibaba.smart.framework.engine.deployment.Deployer;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.extensibility.exception.ExtensionPointLoadException;
import com.alibaba.smart.framework.engine.extensibility.impl.DefaultExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.InstanceManager;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by ettear on 16-4-12.
 */
@Data
public class DefaultSmartEngine implements SmartEngine {

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.NONE)
    private ExtensionPointRegistry extensionPointRegistry;
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.NONE)
    private ClassLoader            classLoader;

    private Deployer        deployer;
    private InstanceManager instanceManager;

    @Override
    public void init(ClassLoader classLoader) throws EngineException{
        ClassLoader frameworkClassLoader=DefaultSmartEngine.class.getClassLoader();
        if (null == classLoader) {
            classLoader = frameworkClassLoader;
        }
        this.classLoader=classLoader;

        ExtensionPointRegistry extensionPointRegistry=new DefaultExtensionPointRegistry();
        try {
            extensionPointRegistry.load(frameworkClassLoader);
            if(classLoader != frameworkClassLoader){
                extensionPointRegistry.load(classLoader);
            }
        }catch (ExtensionPointLoadException loadException){
            throw new EngineException("Init engine failure!",loadException);
        }


    }
}
