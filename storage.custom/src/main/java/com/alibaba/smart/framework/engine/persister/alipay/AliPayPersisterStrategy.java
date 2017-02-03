package com.alibaba.smart.framework.engine.persister.alipay;

import com.alibaba.smart.framework.engine.common.persister.PersisterStrategy;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

import java.util.Map;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  11:17.
 */
public class AliPayPersisterStrategy implements PersisterStrategy {
    @Override
    public ProcessInstance persist(ProcessInstance processInstance, Map<String, Object> request) {

        return processInstance;
    }


}
