package com.alibaba.smart.framework.engine.modules.extensions.transaction.action;

/**
 * @author Leo.yy   Created on 2017/8/2.
 * @description
 * @see
 */
public class ActionResult {

    // 是否推出当前流程
    private boolean breakCurrentProcess;

    // 是否执行成功
    private boolean success;

    // 执行失败的信息
    private String eroMsg;

    public boolean isBreakCurrentProcess() {
        return breakCurrentProcess;
    }

    public void setBreakCurrentProcess(boolean breakCurrentProcess) {
        this.breakCurrentProcess = breakCurrentProcess;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getEroMsg() {
        return eroMsg;
    }

    public void setEroMsg(String eroMsg) {
        this.eroMsg = eroMsg;
    }

    public static ActionResult buildSuccessResult(boolean breakProcess) {
        ActionResult result = new ActionResult();
        result.setSuccess(true);
        result.setBreakCurrentProcess(breakProcess);
        return result;
    }

    public static ActionResult buildFailResult(String msg,boolean breakProcess) {
        ActionResult result = new ActionResult();
        result.setSuccess(false);
        result.setEroMsg(msg);
        result.setBreakCurrentProcess(breakProcess);

        return result;
    }
}
