package com.alibaba.smart.framework.engine.extension.scanner;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.exception.ParseException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.util.ClassLoaderUtil;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class SimpleAnnotationScanner {

    public static final String JAR = "jar";
    public static final String FILE = "file";
    public static final String UTF_8 = "UTF-8";

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleAnnotationScanner.class);

    @Getter
    private static Map<String, ExtensionBindingResult> map = new HashMap<String, ExtensionBindingResult>();

    public static void clear() {
        map.clear();
    }

    public static void scan(String packageName, Class<? extends ExtensionBinding> bindingAnnotationClazz) {

        Set<Class<?>> classSet = null;
        try {
            classSet = scan(packageName);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        for (Class<?> clazz : classSet) {

            boolean present = clazz.isAnnotationPresent(bindingAnnotationClazz);

            if (present) {

                ExtensionBinding bindAnnotation = clazz.getAnnotation(bindingAnnotationClazz);
                String type = bindAnnotation.type();

                ExtensionBindingResult extensionBindingResult = map.get(type);
                if (null == extensionBindingResult) {

                    extensionBindingResult = new ExtensionBindingResult();

                    Map<String, Class> classMap = new HashMap<String, Class>();
                    extensionBindingResult.setBindings(classMap);

                    map.put(type, extensionBindingResult);
                }

                Map<String, Class> bindings = extensionBindingResult.getBindings();
                String name = bindAnnotation.bindingTo().getName();

                if (bindings.get(name) == null) {
                    bindings.put(name, clazz);
                } else {
                    throw new ParseException(
                        "Duplicated key found for " + name + ",because of duplicated annotation or init twice.");
                }

            }

        }

    }

    private static Set<Class<?>> scan(String packageName) throws IOException {

        Set<Class<?>> classes = new LinkedHashSet();

        String formattedPackageDirName = packageName.replace('.', '/');

        Enumeration<URL> dirs = ClassLoaderUtil.getContextClassLoader().getResources(formattedPackageDirName);

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

                                classes.add(ClassLoaderUtil.loadClass(packageName + '.' + className));

                            }
                        }

                    }
                }

            } else {
                throw new EngineException("Not supported protocol: " + protocol);
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
                    ClassLoaderUtil.loadClass(packageName + '.' + className));

            }
        }
    }

}