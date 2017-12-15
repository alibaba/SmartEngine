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
public class MockSaveOrderAction implements SingleTaskAction {

    @Override
    public ActionResult invoke(Map<String, Object> request, SingleTask task) {
        System.out.println("持久化订单");
        return ActionResult.buildSuccessResult(false);
    }

    @Override
    public ActionResult rollback(Map<String, Object> request, SingleTask task) {
        System.out.println("删除订单");
        return ActionResult.buildSuccessResult(false);
    }
}
