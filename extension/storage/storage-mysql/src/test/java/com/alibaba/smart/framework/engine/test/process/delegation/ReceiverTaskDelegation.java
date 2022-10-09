package com.alibaba.smart.framework.engine.test.process.delegation;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ReceiverTaskDelegation implements JavaDelegation {


    @Getter
    private static CopyOnWriteArrayList<String> arrayList = new CopyOnWriteArrayList<String>();

    @Override
    public void execute(ExecutionContext executionContext) {
        Map<String, Object> request = executionContext.getRequest();
        if (null != request) {
            Object o = request.get("input");
            if (o != null) {
                String input = o.toString();
                arrayList.add(input);
            }
        }

    }


}
