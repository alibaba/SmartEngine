package com.alibaba.smart.framework.engine.common.persister;


import java.io.Serializable;


/**
 * Created by 高海军 帝奇 74394 on 2017 February  08:58.
 */
public interface PersisterStrategy {

    void insert(Serializable businessInstanceId , String value) ;

    void update(Serializable businessInstanceId ,String value) ;

    String find(Serializable businessInstanceId);

    void remove(Serializable businessInstanceId);
}
