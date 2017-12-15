package com.alibaba.smart.framework.engine.modules.extensions.transaction.action;


import com.alibaba.smart.framework.engine.modules.extensions.transaction.node.SingleTask;

import java.util.Map;

/**
 * @author Leo.yy   Created on 17/7/31.
 * @description
 * @see
 */
public interface SingleTaskAction extends SmartEngineAction {

    // 逆向回滚接口
    ActionResult rollback(Map<String, Object> request, SingleTask task);

}
