//package com.alibaba.smart.framework.engine.modules.compatible.activiti.provider.multi.instance;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.alibaba.smart.framework.engine.configuration.TaskAssigneeDispatcher;
//import com.alibaba.smart.framework.engine.context.ExecutionContext;
//import com.alibaba.smart.framework.engine.exception.EngineException;
//import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
//import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeCandidateInstance;
//import com.alibaba.smart.framework.engine.modules.bpmn.provider.multi.instance.LoopCollectionProvider;
//import com.alibaba.smart.framework.engine.modules.compatible.activiti.assembly.multi.instance.ActivitiCollection;
//import com.alibaba.smart.framework.engine.pvm.PvmActivity;
//
///**
// * @author ettear
// * Created by ettear on 15/10/2017.
// */
//public class CollectionProvider implements LoopCollectionProvider {
//    private ExtensionPointRegistry extensionPointRegistry;
//    private ActivitiCollection activitiCollection;
//
//    CollectionProvider(ExtensionPointRegistry extensionPointRegistry, ActivitiCollection activitiCollection) {
//        this.extensionPointRegistry = extensionPointRegistry;
//        this.activitiCollection = activitiCollection;
//    }
//
//    @Override
//    public java.util.Collection<Object> getCollection(ExecutionContext context,
//                                                       PvmActivity activity) {
//        TaskAssigneeDispatcher taskAssigneeDispatcher = context.getProcessEngineConfiguration()
//            .getTaskAssigneeDispatcher();
//
//        if (null == taskAssigneeDispatcher) {
//            throw new EngineException(
//                "The taskAssigneeService can't be null for MultiInstanceLoopCharacteristics feature");
//        }
//
//        List<TaskAssigneeCandidateInstance> taskAssigneeCandidateInstanceList = taskAssigneeDispatcher
//            .getTaskAssigneeCandidateInstance(activity.getModel(), context.getRequest());
//
//        List<Object> collection = new ArrayList<Object>(taskAssigneeCandidateInstanceList.size());
//        for (TaskAssigneeCandidateInstance taskAssigneeCandidateInstance : taskAssigneeCandidateInstanceList) {
//            List<TaskAssigneeCandidateInstance> taskAssigneeCandidateInstanceExecuteList = new ArrayList
//                <TaskAssigneeCandidateInstance>(1);
//            taskAssigneeCandidateInstanceExecuteList.add(taskAssigneeCandidateInstance);
//            collection.add(taskAssigneeCandidateInstanceExecuteList);
//        }
//        return collection;
//    }
//}
