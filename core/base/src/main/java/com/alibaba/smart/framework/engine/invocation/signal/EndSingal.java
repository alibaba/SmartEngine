package com.alibaba.smart.framework.engine.invocation.signal;

/**
 * Created by dongdong.zdd on 19/03/2018.
 * dongdong.zdd@alibaba-inc.com
 */
public class EndSingal extends Signal {

    public EndSingal() {
    }

    public EndSingal(String message) {
        super(message);
    }

    @Override
    protected String getSignal() {
        return "end";
    }


}
