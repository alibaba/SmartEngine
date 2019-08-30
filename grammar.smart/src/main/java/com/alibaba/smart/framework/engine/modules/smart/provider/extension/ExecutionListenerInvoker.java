package com.alibaba.smart.framework.engine.modules.smart.provider.extension;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.smart.assembly.extension.ExecutionListener;
import com.alibaba.smart.framework.engine.provider.Invoker;
import com.alibaba.smart.framework.engine.provider.Performer;
/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class ExecutionListenerInvoker implements Invoker {

    private ExecutionListener executionListener;
    private Performer performer;

    public ExecutionListenerInvoker(ExtensionPointRegistry extensionPointRegistry, ExecutionListener executionListener,
                                    Performer performer) {
        this.executionListener = executionListener;
        this.performer = performer;
    }

    //@Override
    //public Object invoke(String method, ExecutionContext context) {
    //    String[] events = this.executionListener.getEvents();
    //    boolean matched = false;
    //    if (null != events) {
    //        for (String event : events) {
    //            if (method.equals(event)) {
    //                matched = true;
    //                break;
    //            }
    //        }
    //    } else {
    //        matched = true;
    //    }
    //    if (matched) {
    //        return this.performer.perform(context);
    //    }
    //    return null;
    //}
}
