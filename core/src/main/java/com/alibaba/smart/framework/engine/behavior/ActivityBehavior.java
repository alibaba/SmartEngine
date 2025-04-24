package com.alibaba.smart.framework.engine.behavior;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface ActivityBehavior {


    /**
     * 仅创建 ei 等相关对象，
     * @param context
     * @param pvmActivity
     * @return
     */
    boolean enter(ExecutionContext context, PvmActivity pvmActivity);

    /**
     *
     * 更新 ei 对象，
     * @param context
     * @param pvmActivity
     * @return
     */
    void execute(ExecutionContext context, PvmActivity pvmActivity);

    /**
     *  在 leave 时，判断如何创建后续节点
     * @param context
     * @param pvmActivity
     * @return
     */
    void leave(ExecutionContext context, PvmActivity pvmActivity);

}
