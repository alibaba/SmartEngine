package com.alibaba.smart.framework.engine.modules.extensions.transaction.action;


import com.alibaba.smart.framework.engine.modules.extensions.transaction.node.SingleTask;

import java.util.Map;

/**
 * @author Leo.yy   Created on 17/7/31.
 * @description
 * @see
 */
public interface SmartEngineAction {

    ActionResult invoke(Map<String, Object> request, SingleTask task);

}
