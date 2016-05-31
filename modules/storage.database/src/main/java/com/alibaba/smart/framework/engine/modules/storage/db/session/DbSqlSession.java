package com.alibaba.smart.framework.engine.modules.storage.db.session;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.smart.framework.engine.modules.storage.db.session.PersistentObjectCacheService.CachedObject;

public class DbSqlSession implements Session {

    private PersistentObjectCacheService cacheService     = new PersistentObjectCacheService();

    protected List<PersistentObject>     insertedObjects  = new ArrayList<PersistentObject>();
    protected List<DeleteOperation>      deleteOperations = new ArrayList<DeleteOperation>();

    private static final Logger          log              = LoggerFactory.getLogger(DbSqlSession.class);

    /**
     *brings the given persistenObject to the top if it already exists
     */
    public void touch(PersistentObject persistentObject) {
        if (persistentObject.getId() == null) {
            throw new RuntimeException("Cannot touch " + persistentObject.getClass() + " with no id");
        }
        if (insertedObjects.contains(persistentObject)) {
            insertedObjects.remove(persistentObject);
            insertedObjects.add(persistentObject);
            cacheService.put(persistentObject, false);
        }
    }


    public void insert(PersistentObject persistentObject) {
        if (persistentObject.getId() == null) {
//            String id = dbSqlSessionFactory.getIdGenerator().getNextId();
            //FIXME
            persistentObject.setId(null);
        }
        insertedObjects.add(persistentObject);
        cacheService.put(persistentObject, false);
    }

    public void update(PersistentObject persistentObject) {
        cacheService.put(persistentObject, false);
    }

    public void delete(PersistentObject persistentObject) {
        for (DeleteOperation deleteOperation : deleteOperations) {
            if (deleteOperation.isSameIdentityWith(persistentObject)) {
                log.debug("skipping redundant delete: {}", persistentObject);
                return; // Skip this delete. It was already added.
            }
        }

        deleteOperations.add(new CheckedDeleteOperation(persistentObject));
    }

    public <T extends PersistentObject> T selectById(Class<T> entityClass, String id) {
        T persistentObject = cacheService.cacheGet(entityClass, id);
        if (persistentObject != null) {
            return persistentObject;
        }
        // String selectStatement = dbSqlSessionFactory.getSelectStatement(entityClass);
        // selectStatement = dbSqlSessionFactory.mapStatement(selectStatement);
        // persistentObject = (T) sqlSession.selectOne(selectStatement, id);
        if (persistentObject == null) {
            return persistentObject;
        }
        cacheService.put(persistentObject, true);
        return persistentObject;
    }

    @Override
    public void flush() {
        List<DeleteOperation> removedOperations = removeUnnecessaryOperations();

        List<PersistentObject> updatedObjects = getUpdatedObjects();

        if (log.isDebugEnabled()) {
            log.debug("flush summary: {} insert, {} update, {} delete.", insertedObjects.size(), updatedObjects.size(),
                      deleteOperations.size());
            for (PersistentObject insertedObject : insertedObjects) {
                log.debug("  insert {}", insertedObject);
            }
            for (PersistentObject updatedObject : updatedObjects) {
                log.debug("  update {}", updatedObject);
            }
            for (DeleteOperation deleteOperation : deleteOperations) {
                log.debug("  {}", deleteOperation);
            }
            log.debug("now executing flush...");
        }

        flushInserts();
        flushUpdates(updatedObjects);
        flushDeletes(removedOperations);
    }

    /**
     * Clears all deleted and inserted objects from the cache, and removes inserts and deletes that cancel each other.
     */
    private List<DeleteOperation> removeUnnecessaryOperations() {
        List<DeleteOperation> removedDeleteOperations = new ArrayList<DeleteOperation>();

        for (Iterator<DeleteOperation> deleteIt = deleteOperations.iterator(); deleteIt.hasNext();) {
            DeleteOperation deleteOperation = deleteIt.next();

            for (Iterator<PersistentObject> insertIt = insertedObjects.iterator(); insertIt.hasNext();) {
                PersistentObject insertedObject = insertIt.next();

                // if the deleted object is inserted,
                if (deleteOperation.isSameIdentityWith(insertedObject)) {
                    // remove the insert and the delete, they cancel each other
                    insertIt.remove();
                    deleteIt.remove();
                    // add removed operations to be able to fire events
                    removedDeleteOperations.add(deleteOperation);
                }
            }

            // in any case, remove the deleted object from the cache
            deleteOperation.clearCache();
        }

        for (PersistentObject insertedObject : insertedObjects) {
            cacheService.cacheRemove(insertedObject.getClass(), insertedObject.getId());
        }

        return removedDeleteOperations;
    }

  
    private List<PersistentObject> getUpdatedObjects() {
        List<PersistentObject> updatedObjects = new ArrayList<PersistentObject>();
        Map<Class<?>, Map<String, CachedObject>> cachedObjects = cacheService.getCachedObjects();
        for (Class<?> clazz : cachedObjects.keySet()) {

            Map<String, CachedObject> classCache = cachedObjects.get(clazz);
            for (CachedObject cachedObject : classCache.values()) {

                PersistentObject persistentObject = cachedObject.getPersistentObject();
                if (!isPersistentObjectDeleted(persistentObject)) {
                    Object originalState = cachedObject.getPersistentObjectState();
                    if (persistentObject.getPersistentState() != null
                        && !persistentObject.getPersistentState().equals(originalState)) {
                        updatedObjects.add(persistentObject);
                    } else {
                        log.trace("loaded object '{}' was not updated", persistentObject);
                    }
                }

            }

        }
        return updatedObjects;
    }

    private boolean isPersistentObjectDeleted(PersistentObject persistentObject) {
        for (DeleteOperation deleteOperation : deleteOperations) {
            if (deleteOperation.isSameIdentityWith(persistentObject)) {
                return true;
            }
        }
        return false;
    }

    public <T extends PersistentObject> List<T> pruneDeletedEntities(List<T> listToPrune) {
        List<T> prunedList = new ArrayList<T>(listToPrune);
        for (T potentiallyDeleted : listToPrune) {
            for (DeleteOperation deleteOperation : deleteOperations) {

                if (deleteOperation.isSameIdentityWith(potentiallyDeleted)) {
                    prunedList.remove(potentiallyDeleted);
                }

            }
        }
        return prunedList;
    }

    private void flushInserts() {
        for (PersistentObject insertedObject : insertedObjects) {
            // String insertStatement = dbSqlSessionFactory.getInsertStatement(insertedObject);
            // insertStatement = dbSqlSessionFactory.mapStatement(insertStatement);
            //
            // if (insertStatement==null) {
            // throw new
            // ActivitiException("no insert statement for "+insertedObject.getClass()+" in the ibatis mapping files");
            // }
            //
            // log.debug("inserting: {}", insertedObject);
            // sqlSession.insert(insertStatement, insertedObject);
            //
            // // See http://jira.codehaus.org/browse/ACT-1290
            // if (insertedObject instanceof HasRevision) {
            // ((HasRevision) insertedObject).setRevision(((HasRevision) insertedObject).getRevisionNext());
            // }
        }
        insertedObjects.clear();
    }

    private void flushUpdates(List<PersistentObject> updatedObjects) {
        for (PersistentObject updatedObject : updatedObjects) {
            // String updateStatement = dbSqlSessionFactory.getUpdateStatement(updatedObject);
            // updateStatement = dbSqlSessionFactory.mapStatement(updateStatement);
            //
            // if (updateStatement==null) {
            // throw new
            // ActivitiException("no update statement for "+updatedObject.getClass()+" in the ibatis mapping files");
            // }
            //
            // log.debug("updating: {}", updatedObject);
            // int updatedRecords = sqlSession.update(updateStatement, updatedObject);
            // if (updatedRecords!=1) {
            // throw new ActivitiOptimisticLockingException(updatedObject +
            // " was updated by another transaction concurrently");
            // }
            //
            // // See http://jira.codehaus.org/browse/ACT-1290
            // if (updatedObject instanceof HasRevision) {
            // ((HasRevision) updatedObject).setRevision(((HasRevision) updatedObject).getRevisionNext());
            // }

        }
        updatedObjects.clear();
    }

    private void flushDeletes(List<DeleteOperation> removedOperations) {

        // TODO
        flushRegularDeletes();

        deleteOperations.clear();
    }

    private void flushRegularDeletes() {
        List<DeleteOperation> optimizedDeleteOperations = optimizeDeleteOperations(deleteOperations);
        for (DeleteOperation delete : optimizedDeleteOperations) {
            // for (DeleteOperation delete : deleteOperations) {
            log.debug("executing: {}", delete);

            delete.execute();

        }
    }
    
    /**
     * Optimizes the given delete operations: for example, if there are two deletes for two different variables, merges
     * this into one bulk delete which improves performance
     */
    private List<DeleteOperation> optimizeDeleteOperations(List<DeleteOperation> deleteOperations) {

        // No optimization possible for 0 or 1 operations
        if (deleteOperations.size() <= 1) {
            return deleteOperations;
        }

        List<DeleteOperation> optimizedDeleteOperations = new ArrayList<DeleteOperation>();
        boolean[] checkedIndices = new boolean[deleteOperations.size()];
        for (int i = 0; i < deleteOperations.size(); i++) {

            if (checkedIndices[i] == true) {
                continue;
            }

            DeleteOperation deleteOperation = deleteOperations.get(i);
            boolean couldOptimize = false;
            // if (deleteOperation instanceof CheckedDeleteOperation) {
            //
            // PersistentObject persistentObject = ((CheckedDeleteOperation) deleteOperation).getPersistentObject();
            // if (persistentObject instanceof BulkDeleteable) {
            // String bulkDeleteStatement = dbSqlSessionFactory.getBulkDeleteStatement(persistentObject.getClass());
            // bulkDeleteStatement = dbSqlSessionFactory.mapStatement(bulkDeleteStatement);
            // if (bulkDeleteStatement != null) {
            // BulkCheckedDeleteOperation bulkCheckedDeleteOperation = null;
            //
            // // Find all objects of the same type
            // for (int j = 0; j < deleteOperations.size(); j++) {
            // DeleteOperation otherDeleteOperation = deleteOperations.get(j);
            // if (j != i && checkedIndices[j] == false
            // && otherDeleteOperation instanceof CheckedDeleteOperation) {
            // PersistentObject otherPersistentObject = ((CheckedDeleteOperation)
            // otherDeleteOperation).getPersistentObject();
            // if (otherPersistentObject.getClass().equals(persistentObject.getClass())) {
            // if (bulkCheckedDeleteOperation == null) {
            // bulkCheckedDeleteOperation = new BulkCheckedDeleteOperation(
            // persistentObject.getClass());
            // bulkCheckedDeleteOperation.addPersistentObject(persistentObject);
            // optimizedDeleteOperations.add(bulkCheckedDeleteOperation);
            // }
            // couldOptimize = true;
            // bulkCheckedDeleteOperation.addPersistentObject(otherPersistentObject);
            // checkedIndices[j] = true;
            // } else {
            // // We may only optimize subsequent delete operations of the same type, to prevent
            // // messing up
            // // the order of deletes of related entities which may depend on the referenced
            // // entity being deleted before
            // break;
            // }
            // }
            //
            // }
            // }
            // }
            // }

            if (!couldOptimize) {
                optimizedDeleteOperations.add(deleteOperation);
            }
            checkedIndices[i] = true;

        }
        return optimizedDeleteOperations;
    }


    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

}
