package com.alibaba.smart.framework.engine.persister.util;

import com.alibaba.smart.framework.engine.instance.impl.DefaultActivityInstance;
import com.alibaba.smart.framework.engine.instance.impl.DefaultExecutionInstance;
import com.alibaba.smart.framework.engine.instance.impl.DefaultProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  11:54.
 */
public class InstanceSerializer {


    public static String serialize(ProcessInstance processInstance) {

        StringBuilder stringBuilder = new StringBuilder(64);
        //设置序列化版本号
        stringBuilder.append("v0|");

        //序列化流程实例
        stringBuilder.append(processInstance.getInstanceId()).append(",");
        stringBuilder.append(processInstance.getProcessDefinitionIdAndVersion()).append(",");
        stringBuilder.append(processInstance.getStatus()).append("|");

        //----分割线--- 上部分完成流程实例的序列化,下部分完成活动实例和执行实例的序列化。

        List<ActivityInstance> activityInstances = processInstance.getNewActivityInstances();
        for (ActivityInstance activityInstance : activityInstances) {


            List<ExecutionInstance> executionInstances =    activityInstance.getExecutionInstanceList();
            for (ExecutionInstance executionInstance : executionInstances) {
                if (null != executionInstance) {
                    boolean active = executionInstance.isActive();

                    if(active){
                        //注意: 这里仅保存了需要被执行的实例,历史的activityInstance在这里并没有保存。在阿里的海量数据业务中,也通常不需要。
                        stringBuilder.append(activityInstance.getInstanceId()).append(",");
                        stringBuilder.append(activityInstance.getProcessDefinitionActivityId()).append(",");
                        stringBuilder.append(executionInstance.getInstanceId()).append(",");
                        stringBuilder.append(active).append(",");

                        stringBuilder.append("|");
                    }

                }
            }




        }

        return stringBuilder.toString();
    }


    public static ProcessInstance deserializeAll(String serializeString) {
        ProcessInstance processInstance = new DefaultProcessInstance();

        StringTokenizer st = new StringTokenizer(serializeString, "|");
        String version = st.nextToken();
        String processInstanceValue = st.nextToken();

        buildProcessInstance(processInstance, processInstanceValue);


        buildActivityInstanceAndExecutionInstance(st, processInstance);


        return processInstance;

    }



    public static ProcessInstance deserializeProcessInstance(String serializeString) {
        ProcessInstance processInstance = new DefaultProcessInstance();

        StringTokenizer st = new StringTokenizer(serializeString, "|");
        String serializeVersion = st.nextToken();
        String processInstanceValue = st.nextToken();

        buildProcessInstance(processInstance, processInstanceValue);

        return processInstance;

    }

    public static List<ActivityInstance>  deserializeActivityInstances(String serializeString) {

        StringTokenizer st = new StringTokenizer(serializeString, "|");
        String version = st.nextToken();
        String processInstanceValue = st.nextToken();

        List<ActivityInstance> activityInstances = new ArrayList<ActivityInstance>();

        while (st.hasMoreTokens()) {
            ActivityInstance activityInstance = buildActivityInstanceAndExecutionInstance(st);

            activityInstances.add(activityInstance);
        }

        return activityInstances;

    }


    public static List<ExecutionInstance>  deserializeExecutionInstances(String serializeString) {


        List<ActivityInstance>  activityInstances  = deserializeActivityInstances(serializeString);

        List<ExecutionInstance> executionInstances1 = new ArrayList<ExecutionInstance>(activityInstances.size());
        for (ActivityInstance activityInstance : activityInstances) {

            List<ExecutionInstance> executionInstances =    activityInstance.getExecutionInstanceList();
            for (ExecutionInstance executionInstance : executionInstances) {
                executionInstances1.add(executionInstance);

            }

        }

        return executionInstances1;

    }

    private static void buildActivityInstanceAndExecutionInstance(StringTokenizer st, ProcessInstance processInstance) {
        while (st.hasMoreTokens()) {
            ActivityInstance activityInstance = buildActivityInstanceAndExecutionInstance(st);

            processInstance.getNewActivityInstances().add(activityInstance);
        }
    }

    private static ActivityInstance buildActivityInstanceAndExecutionInstance(StringTokenizer st) {
        String activityInstanceAndExecutionInstance = st.nextToken();
        StringTokenizer st1 = new StringTokenizer(activityInstanceAndExecutionInstance, ",");
        ActivityInstance activityInstance = new DefaultActivityInstance();
        activityInstance.setInstanceId(Long.valueOf(st1.nextToken()));
        String activityId = st1.nextToken();
        activityInstance.setProcessDefinitionActivityId(activityId);
        ExecutionInstance executionInstance = buildExecutionInstance(st1, activityId);

        //FIXME 有点问题、
        List<ExecutionInstance> executionInstanceList = new ArrayList<ExecutionInstance>(2);
        executionInstanceList.add(executionInstance);
        activityInstance.setExecutionInstanceList(executionInstanceList);

        return activityInstance;
    }

    private static ExecutionInstance buildExecutionInstance(StringTokenizer st1, String activityId) {
        ExecutionInstance executionInstance = new DefaultExecutionInstance();
        executionInstance.setInstanceId(Long.valueOf(st1.nextToken()));
        executionInstance.setProcessDefinitionActivityId(activityId);
        executionInstance.setActive(Boolean.valueOf(st1.nextToken()));
        return executionInstance;
    }

    private static void buildProcessInstance(ProcessInstance processInstance, String processInstanceSerialiable) {
        StringTokenizer st = new StringTokenizer(processInstanceSerialiable, ",");
        processInstance.setInstanceId(Long.valueOf(st.nextToken()));
        String processDefinitionIdAndVersion = st.nextToken();
        processInstance.setProcessDefinitionIdAndVersion(processDefinitionIdAndVersion);
        processInstance.setStatus(InstanceStatus.valueOf(st.nextToken()));
    }


}
