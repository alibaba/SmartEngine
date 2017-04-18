package com.alibaba.smart.framework.engine.modules.bpmn;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by dongdongzdd on 16/9/8.
 */
public class CreateOrder {


    @Resource
    private Workflow workflow;


    public void create(Map<String,Object> context) {
        System.out.println("\"run success!!!!!!!!!!!!!);");
        context.entrySet().stream().forEach(p -> System.out.println(p.toString()));

        ProcessDefinition processDefinition = (ProcessDefinition) context.get("processDefinition");
        String activity = (String) context.get("activityId");
        String id = (String) context.get("id");

        ProcessInstance push = workflow.getEngine().getProcessService().pushActivityOnRam(
                processDefinition,
                id
        );
        System.out.println("push result : "+push.toString());


    }


}
