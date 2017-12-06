package com.alibaba.smart.framework.engine.modules.extensions.transaction.test;

import com.alibaba.smart.framework.engine.modules.extensions.transaction.action.ActionResult;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.action.SingleTaskAction;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.node.SingleTask;

import java.util.Map;

/**
 * @author Leo.yy   Created on 2017/12/6.
 * @description
 * @see
 */
public class MockCreateOrderAction implements SingleTaskAction {

    @Override
    public ActionResult invoke(Map<String, Object> request, SingleTask task) {
        System.out.println("调用CP创建订单");
        return ActionResult.buildSuccessResult(false);
    }

    @Override
    public ActionResult rollback(Map<String, Object> request, SingleTask task) {
        System.out.println("调用CP取消订单");
        return ActionResult.buildSuccessResult(false);
    }
}
