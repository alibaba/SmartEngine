package com.alibaba.smart.framework.engine.modules.storage.db.session;
 /**
     * A {@link DeleteOperation} that checks for concurrent modifications if the persistent object implements {@link HasRevision}.
     * That is, it employs optimisting concurrency control. Used when the persistent object has been fetched already.
     */
    public class CheckedDeleteOperation implements DeleteOperation {
      protected final PersistentObject persistentObject;
      
      public CheckedDeleteOperation(PersistentObject persistentObject) {
        this.persistentObject = persistentObject;
      }
      
      @Override
      public boolean isSameIdentityWith(PersistentObject other) {
        return persistentObject.getClass().equals(other.getClass())
            && persistentObject.getId().equals(other.getId());
      }

      @Override
      public void clearCache() {
          //FIXME
//        cacheRemove(persistentObject.getClass(), persistentObject.getId());
      }
      
      public void execute() {
//        String deleteStatement = dbSqlSessionFactory.getDeleteStatement(persistentObject.getClass());
//        deleteStatement = dbSqlSessionFactory.mapStatement(deleteStatement);
//        if (deleteStatement == null) {
//          throw new ActivitiException("no delete statement for " + persistentObject.getClass() + " in the ibatis mapping files");
//        }
//        
//        // It only makes sense to check for optimistic locking exceptions for objects that actually have a revision
//        if (persistentObject instanceof HasRevision) {
//          int nrOfRowsDeleted = sqlSession.delete(deleteStatement, persistentObject);
//          if (nrOfRowsDeleted == 0) {
//            throw new ActivitiOptimisticLockingException(persistentObject + " was updated by another transaction concurrently");
//          }
//        } else {
//          sqlSession.delete(deleteStatement, persistentObject);
//        }
      }

      public PersistentObject getPersistentObject() {
        return persistentObject;
      }

      @Override
      public String toString() {
        return "delete " + persistentObject;
      }
    }