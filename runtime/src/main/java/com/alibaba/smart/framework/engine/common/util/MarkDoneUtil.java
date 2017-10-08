package com.alibaba.smart.framework.engine.common.util;

import java.util.Date;

import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;

/**
 * Created by 高海军 帝奇 74394 on 2017 June  10:35.
 */
public class MarkDoneUtil {

    public   static  void markDone(ExecutionInstance executionInstance,ExecutionInstanceStorage executionInstanceStorage) {
        Date completeDate = DateUtil.getCurrentDate();
        executionInstance.setCompleteTime(completeDate);
        executionInstance.setActive(false);
        executionInstanceStorage.update(executionInstance);


    }

}
