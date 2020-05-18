package com.alibaba.smart.framework.engine.extension.scanner;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.configuration.scanner.ExtensionBindingResult;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.util.ClassUtil;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleAnnotationScanner implements AnnotationScanner {

    public static final String JAR = "jar";
    public static final String FILE = "file";
    public static final String UTF_8 = "UTF-8";

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleAnnotationScanner.class);


    @Getter
    private Map<String, ExtensionBindingResult> scanResult = new HashMap<String, ExtensionBindingResult>();

    private String[] packageNameList;

    public SimpleAnnotationScanner(String... packageNameList) {this.packageNameList = packageNameList;}

    protected static Set<Class<?>> scan(String... packageNames) throws IOException {

        Set<Class<?>> classes = new LinkedHashSet();

        for (String packageName : packageNames) {

            String formattedPackageDirName = packageName.replace('.', '/');

            Enumeration<URL> dirs = ClassUtil.getContextClassLoader().getResources(formattedPackageDirName);

            while (dirs.hasMoreElements()) {

                URL url = dirs.nextElement();
                String protocol = url.getProtocol();

                if (FILE.equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), UTF_8);

                    scanFiles(packageName, filePath, true, classes);

                } else if (JAR.equals(protocol)) {

                    JarFile jar = ((JarURLConnection)url.openConnection()).getJarFile();

                    Enumeration<JarEntry> entries = jar.entries();

                    while (entries.hasMoreElements()) {

                        JarEntry entry = entries.nextElement();
                        String entryName = entry.getName();

                        if (entryName.startsWith(formattedPackageDirName)) {
                            int idx = entryName.lastIndexOf('/');

                            if (idx != -1) {

                                packageName = entryName.substring(0, idx).replace('/', '.');

                                if (entryName.endsWith(".class") && !entry.isDirectory()) {

                                    String className = entryName.substring(packageName.length() + 1,
                                        entryName.length() - 6);

                                    classes.add(ClassUtil.loadClass(packageName + '.' + className));

                                }
                            }

                        }
                    }

                } else {
                    throw new EngineException("Not supported protocol: " + protocol);
                }
            }
        }



        return classes;
    }

    private static void scanFiles(String packageName, String packagePath,
                                  final boolean recursive,
                                  Set<Class<?>> classes) {
        File dir = new File(packagePath);

        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        File[] filteredFiles = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });

        for (File file : filteredFiles) {

            if (file.isDirectory()) {
                scanFiles(packageName + "." + file.getName(), file.getAbsolutePath(), recursive,
                    classes);
            } else {
                String className = file.getName().substring(0, file.getName().length() - 6);

                classes.add(
                    ClassUtil.loadClass(packageName + '.' + className));

            }
        }
    }

    public void clear() {
        scanResult.clear();
    }

    public void scan(
        ProcessEngineConfiguration processEngineConfiguration, Class<? extends Annotation> targetAnnotationType) {

        Set<Class<?>> classSet;
        try {
            classSet = scan(this.packageNameList);
        } catch (IOException e) {
            throw new EngineException(e.getMessage(), e);
        }

        for (Class<?> currentClazz : classSet) {

            boolean present = currentClazz.isAnnotationPresent(targetAnnotationType);

            if (present) {

                ExtensionBinding currentBindingAnnotation = (ExtensionBinding)currentClazz.getAnnotation(targetAnnotationType);
                String group = currentBindingAnnotation.group();

                ExtensionBindingResult extensionBindingResult = scanResult.get(group);
                if (null == extensionBindingResult) {

                    extensionBindingResult = new ExtensionBindingResult();

                    Map<Class, Object> bindingMap = new HashMap<Class, Object>();
                    extensionBindingResult.setBindingMap(bindingMap);

                    scanResult.put(group, extensionBindingResult);
                }

                Map<Class, Object> bindingMap = extensionBindingResult.getBindingMap();

                Class bindKeyClass = currentBindingAnnotation.bindKey();

                Object objectInBindingMap = bindingMap.get(bindKeyClass);
                if (objectInBindingMap == null) {

                    put(processEngineConfiguration, currentClazz, bindingMap, bindKeyClass);

                } else {
                    handleDuplicatedKey( processEngineConfiguration,bindingMap,  currentBindingAnnotation, currentClazz,objectInBindingMap,targetAnnotationType);
                }

            }

        }

    }

    private void put(ProcessEngineConfiguration processEngineConfiguration, Class<?> currentClazz,
                     Map<Class, Object> bindingMap, Class bindKeyClass) {
        boolean pecFound = false;

        Class tempClazz = currentClazz;

        while (tempClazz != null && !tempClazz.equals(Object.class)){

            Class<?>[] interfaces = tempClazz.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                if (anInterface.equals(ProcessEngineConfigurationAware.class)) {
                    pecFound = true;
                    break;
                }
            }
            tempClazz = tempClazz.getSuperclass();
        }

        Object newInstance  = ClassUtil.createNewInstance(currentClazz);
        if (pecFound) {
            ((ProcessEngineConfigurationAware)newInstance).setProcessEngineConfiguration(
                processEngineConfiguration);
        }

        bindingMap.put(bindKeyClass, newInstance);
    }

    protected void handleDuplicatedKey(ProcessEngineConfiguration processEngineConfiguration,Map<Class, Object> bindingMap, ExtensionBinding currentBindingAnnotation, Class targetClass,Object objectInBindingMap,Class<? extends Annotation> targetAnnotationType) {

        String group = currentBindingAnnotation.group();
        Class bindKeyClass = currentBindingAnnotation.bindKey();

        ExtensionBinding exBindingAnnotation = (ExtensionBinding)objectInBindingMap.getClass().getAnnotation(targetAnnotationType);

        if(currentBindingAnnotation.priority() < exBindingAnnotation.priority()){
            LOGGER.warn("Because of lower priority, current extension  {} is ignored, still using the extension {} " ,targetClass,objectInBindingMap.getClass());
        }

       else if(currentBindingAnnotation.priority() == exBindingAnnotation.priority()){
            throw new EngineException(

                "Duplicated bindKeyClass  found " + bindKeyClass + " for group " + group
                    + ", because of duplicated annotation or init twice.");
        }else {

            put(processEngineConfiguration, targetClass, bindingMap, bindKeyClass);


            LOGGER.warn("Because of higher priority, current extension  {} is replaced, now using the new extension {} " ,objectInBindingMap.getClass(),targetClass);


        }


    }

    public <T> T getExtensionPoint(String group, Class<T> clazz) {
        ExtensionBindingResult extensionBindingResult = this.scanResult.get(group);
        Map<Class, Object> bindingMap = extensionBindingResult.getBindingMap();
        return (T)bindingMap.get(clazz);
    }

    public Object getObject(String group, Class clazz) {
        return this.scanResult.get(group).getBindingMap().get(clazz);

    }

}