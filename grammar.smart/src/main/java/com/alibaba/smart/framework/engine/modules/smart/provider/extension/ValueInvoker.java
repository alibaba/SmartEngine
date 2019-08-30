package com.alibaba.smart.framework.engine.modules.smart.provider.extension;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.smart.assembly.extension.Value;
import com.alibaba.smart.framework.engine.provider.Invoker;
/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class ValueInvoker implements Invoker {

    private Value value;

    public ValueInvoker(ExtensionPointRegistry extensionPointRegistry, Value value) {
        this.value = value;
    }

    //@Override
    //public Object invoke(String method, ExecutionContext context) {
    //    Map<String,Object> request=context.getRequest();
    //    if(null==request){
    //        request=new HashMap<String, Object>();
    //        context.setRequest(request);
    //    }
    //    request.put(value.getName(),value.getValue());
    //    return null;
    //}
}
