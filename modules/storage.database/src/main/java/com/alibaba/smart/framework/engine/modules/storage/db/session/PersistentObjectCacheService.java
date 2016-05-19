package com.alibaba.smart.framework.engine.modules.storage.db.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

public class PersistentObjectCacheService {

    @Getter
    protected Map<Class<?>, Map<String, CachedObject>> cachedObjects = new HashMap<Class<?>, Map<String, CachedObject>>();

    protected CachedObject cachePut(PersistentObject persistentObject, boolean storeState) {
        Map<String, CachedObject> classCache = cachedObjects.get(persistentObject.getClass());
        if (classCache == null) {
            classCache = new HashMap<String, CachedObject>();
            cachedObjects.put(persistentObject.getClass(), classCache);
        }
        CachedObject cachedObject = new CachedObject(persistentObject, storeState);
        classCache.put(persistentObject.getId(), cachedObject);
        return cachedObject;
    }

    /**
     * returns the object in the cache. if this object was loaded before, then the original object is returned. if this
     * is the first time this object is loaded, then the loadedObject is added to the cache.
     */
    protected PersistentObject cacheFilter(PersistentObject persistentObject) {
        PersistentObject cachedPersistentObject = cacheGet(persistentObject.getClass(), persistentObject.getId());
        if (cachedPersistentObject != null) {
            return cachedPersistentObject;
        }
        cachePut(persistentObject, true);
        return persistentObject;
    }

    @SuppressWarnings("unchecked")
    protected <T> T cacheGet(Class<T> entityClass, String id) {
        CachedObject cachedObject = null;
        Map<String, CachedObject> classCache = cachedObjects.get(entityClass);
        if (classCache != null) {
            cachedObject = classCache.get(id);
        }
        if (cachedObject != null) {
            return (T) cachedObject.getPersistentObject();
        }
        return null;
    }

    protected void cacheRemove(Class<?> persistentObjectClass, String persistentObjectId) {
        Map<String, CachedObject> classCache = cachedObjects.get(persistentObjectClass);
        if (classCache == null) {
            return;
        }
        classCache.remove(persistentObjectId);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> findInCache(Class<T> entityClass) {
        Map<String, CachedObject> classCache = cachedObjects.get(entityClass);
        if (classCache != null) {
            List<T> entities = new ArrayList<T>(classCache.size());
            for (CachedObject cachedObject : classCache.values()) {
                entities.add((T) cachedObject.getPersistentObject());
            }
            return entities;
        }
        return Collections.emptyList();
    }

    public <T> T findInCache(Class<T> entityClass, String id) {
        return cacheGet(entityClass, id);
    }

    public static class CachedObject {

        protected PersistentObject persistentObject;
        protected Object           persistentObjectState;

        public CachedObject(PersistentObject persistentObject, boolean storeState) {
            this.persistentObject = persistentObject;
            if (storeState) {
                this.persistentObjectState = persistentObject.getPersistentState();
            }
        }

        public PersistentObject getPersistentObject() {
            return persistentObject;
        }

        public Object getPersistentObjectState() {
            return persistentObjectState;
        }
    }
}
