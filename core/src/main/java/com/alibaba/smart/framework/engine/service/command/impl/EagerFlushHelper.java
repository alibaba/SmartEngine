package com.alibaba.smart.framework.engine.service.command.impl;

import com.alibaba.smart.framework.engine.configuration.ConfigurationOption;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.VariablePersister;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.constant.AdHocConstant;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultVariableInstance;
import com.alibaba.smart.framework.engine.instance.storage.*;
import com.alibaba.smart.framework.engine.model.instance.*;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static com.alibaba.smart.framework.engine.service.command.impl.CommonServiceHelper.justCreateEIAndTIAndTAI;
import static com.alibaba.smart.framework.engine.service.command.impl.CommonServiceHelper.justCreateTIAndTAI;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  20:38.
 */
public abstract  class EagerFlushHelper {

    public static void createProcessInstanceAndVariableInstance(ProcessInstance processInstance, Map<String, Object> request,
                                                   ProcessEngineConfiguration processEngineConfiguration) {

        ConfigurationOption configurationOption = processEngineConfiguration
                .getOptionContainer().get(ConfigurationOption.EAGER_FLUSH_ENABLED_OPTION.getId());

        if (!configurationOption.isEnabled()){
            return ;
        }

        //TUNE 可以在对象创建时初始化,但是这里依赖稍微有点问题
        AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();
        ProcessInstanceStorage processInstanceStorage = annotationScanner.getExtensionPoint(
                ExtensionConstant.COMMON,ProcessInstanceStorage.class);

        ProcessInstance newProcessInstance =  processInstanceStorage.insert(processInstance, processEngineConfiguration);

        CommonServiceHelper.persistVariableInstanceIfPossible(request, processEngineConfiguration,
                newProcessInstance, AdHocConstant.DEFAULT_ZERO_VALUE);

    }

    public static void update(ProcessInstance processInstance,
                                                                ProcessEngineConfiguration processEngineConfiguration) {

        ConfigurationOption configurationOption = processEngineConfiguration
                .getOptionContainer().get(ConfigurationOption.EAGER_FLUSH_ENABLED_OPTION.getId());

        if (!configurationOption.isEnabled()){
            return ;
        }

        //TUNE 可以在对象创建时初始化,但是这里依赖稍微有点问题
        AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();
        ProcessInstanceStorage processInstanceStorage = annotationScanner.getExtensionPoint(
                ExtensionConstant.COMMON,ProcessInstanceStorage.class);

        ProcessInstance newProcessInstance =  processInstanceStorage.update(processInstance, processEngineConfiguration);



    }

    public static void createActivityInstance(ActivityInstance activityInstance,
                                                                ProcessEngineConfiguration processEngineConfiguration) {

        ConfigurationOption configurationOption = processEngineConfiguration
                .getOptionContainer().get(ConfigurationOption.EAGER_FLUSH_ENABLED_OPTION.getId());

        if (!configurationOption.isEnabled()){
            return ;
        }

        //TUNE 可以在对象创建时初始化,但是这里依赖稍微有点问题
        AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();
        ActivityInstanceStorage activityInstanceStorage= annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,ActivityInstanceStorage.class);


        activityInstanceStorage.insert(activityInstance,processEngineConfiguration );


    }

    public static void createExecutionInstance(ExecutionInstance executionInstance,
                                              ProcessEngineConfiguration processEngineConfiguration) {

        ConfigurationOption configurationOption = processEngineConfiguration
                .getOptionContainer().get(ConfigurationOption.EAGER_FLUSH_ENABLED_OPTION.getId());

        if (!configurationOption.isEnabled()){
            return ;
        }

        //TUNE 可以在对象创建时初始化,但是这里依赖稍微有点问题
        AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();
        ExecutionInstanceStorage executionInstanceStorage=annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,ExecutionInstanceStorage.class);

        executionInstanceStorage.insert(executionInstance,processEngineConfiguration );

    }


    public static void createTaskInstance(TaskInstance instance,
                                               ProcessEngineConfiguration processEngineConfiguration) {

        ConfigurationOption configurationOption = processEngineConfiguration
                .getOptionContainer().get(ConfigurationOption.EAGER_FLUSH_ENABLED_OPTION.getId());

        if (!configurationOption.isEnabled()){
            return ;
        }

        //TUNE 可以在对象创建时初始化,但是这里依赖稍微有点问题
        AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();
        TaskInstanceStorage taskInstanceStorage=annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,TaskInstanceStorage.class);
        TaskAssigneeStorage taskAssigneeStorage = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,TaskAssigneeStorage.class);

        taskInstanceStorage.insert(instance,processEngineConfiguration );

    }


    public static void createTaskAssignee(TaskAssigneeInstance instance,
                                          ProcessEngineConfiguration processEngineConfiguration) {

        ConfigurationOption configurationOption = processEngineConfiguration
                .getOptionContainer().get(ConfigurationOption.EAGER_FLUSH_ENABLED_OPTION.getId());

        if (!configurationOption.isEnabled()){
            return ;
        }

        //TUNE 可以在对象创建时初始化,但是这里依赖稍微有点问题
        AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();
        TaskAssigneeStorage taskAssigneeStorage = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,TaskAssigneeStorage.class);

        taskAssigneeStorage.insert(instance,processEngineConfiguration );

    }

    public static void createEIAndTiAndTAI(ExecutionInstanceStorage executionInstanceStorage, TaskInstanceStorage taskInstanceStorage, TaskAssigneeStorage taskAssigneeStorage, ExecutionInstance executionInstance, ProcessEngineConfiguration processEngineConfiguration) {
        ConfigurationOption configurationOption = processEngineConfiguration
                .getOptionContainer().get(ConfigurationOption.EAGER_FLUSH_ENABLED_OPTION.getId());

        if (!configurationOption.isEnabled()){
            return ;
        }


        justCreateEIAndTIAndTAI(executionInstanceStorage, taskInstanceStorage, taskAssigneeStorage, executionInstance, processEngineConfiguration);
    }




    public static void createTIAndTAI( TaskInstanceStorage taskInstanceStorage, TaskAssigneeStorage taskAssigneeStorage, ExecutionInstance executionInstance, ProcessEngineConfiguration processEngineConfiguration) {
        ConfigurationOption configurationOption = processEngineConfiguration
                .getOptionContainer().get(ConfigurationOption.EAGER_FLUSH_ENABLED_OPTION.getId());

        if (!configurationOption.isEnabled()){
            return ;
        }


        justCreateTIAndTAI(taskInstanceStorage, taskAssigneeStorage, executionInstance, processEngineConfiguration);
    }




}
