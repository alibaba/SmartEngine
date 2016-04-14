package com.alibaba.smart.framework.engine;

import com.alibaba.smart.framework.engine.deployment.Deployer;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.extensibility.exception.ExtensionPointLoadException;
import com.alibaba.smart.framework.engine.extensibility.impl.DefaultExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.InstanceManager;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default Smart Engine
 * Created by ettear on 16-4-12.
 */
public class DefaultSmartEngine implements SmartEngine {

    private final static String DEFAULT_MODULE="framework";

    private ExtensionPointRegistry extensionPointRegistry;
    private Map<String, ClassLoader> classLoaders = new ConcurrentHashMap<>();

    private Deployer        deployer;
    private InstanceManager instanceManager;

    public DefaultSmartEngine() throws EngineException {
        this.extensionPointRegistry = new DefaultExtensionPointRegistry(this);
        ClassLoader classLoader = DefaultSmartEngine.class.getClassLoader();
        this.install(DEFAULT_MODULE, classLoader);

        this.deployer = this.extensionPointRegistry.getExtensionPoint(Deployer.class);
        this.instanceManager = this.extensionPointRegistry.getExtensionPoint(InstanceManager.class);
    }

    @Override
    public void install(String moduleName, ClassLoader classLoader) throws EngineException {
        if (StringUtils.isBlank(moduleName)) {
            moduleName = DEFAULT_MODULE;
        }
        if(!this.classLoaders.containsKey(moduleName)){
            try {
                boolean loaded=false;
                for (ClassLoader loader : classLoaders.values()) {
                    if(loader==classLoader){
                        loaded=true;
                        break;
                    }
                }
                this.classLoaders.put(moduleName,classLoader);
                if (!loaded) {
                    this.extensionPointRegistry.load(moduleName,classLoader);
                }
            } catch (ExtensionPointLoadException loadException) {
                throw new EngineException("Init engine failure!", loadException);
            }

        }
    }

    @Override
    public void start() {
        this.extensionPointRegistry.start();
    }

    @Override
    public void stop() {
        this.extensionPointRegistry.stop();
    }

    @Override
    public ExtensionPointRegistry getExtensionPointRegistry() {
        return extensionPointRegistry;
    }

    @Override
    public ClassLoader getClassLoader(String moduleName) {
        if (StringUtils.isBlank(moduleName)) {
            moduleName = DEFAULT_MODULE;
        }
        return this.classLoaders.get(moduleName);
    }

    @Override
    public Deployer getDeployer() {
        return deployer;
    }

    @Override
    public InstanceManager getInstanceManager() {
        return instanceManager;
    }
}
