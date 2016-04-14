///* Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// * 
// *      http://www.apache.org/licenses/LICENSE-2.0
// * 
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.xx.test;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.activiti.engine.FormService;
//import org.activiti.engine.HistoryService;
//import org.activiti.engine.IdentityService;
//import org.activiti.engine.ManagementService;
//import org.activiti.engine.ProcessEngine;
//import org.activiti.engine.ProcessEngines;
//import org.activiti.engine.RepositoryService;
//import org.activiti.engine.RuntimeService;
//import org.activiti.engine.TaskService;
//import org.activiti.engine.impl.ProcessEngineImpl;
//import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
//import org.activiti.engine.impl.test.TestHelper;
//import org.activiti.engine.runtime.ProcessInstance;
//import org.activiti.engine.task.Task;
//import org.activiti.engine.test.Deployment;
//import org.junit.Assert;
//import org.junit.Test;
//
///**
// * Example of using the exclusive gateway.
// * 
// * @author Joram Barrez
// */
//public class MyGateWayTest   {
//	
//	  protected ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//	  
//	  protected String deploymentIdFromDeploymentAnnotation;
//	  protected List<String> deploymentIdsForAutoCleanup = new ArrayList<String>();
//	  protected Throwable exception;
//
//	  protected ProcessEngineConfigurationImpl processEngineConfiguration;
//	  protected RepositoryService repositoryService;
//	  protected RuntimeService runtimeService;
//	  protected TaskService taskService;
//	  protected FormService formService;
//	  protected HistoryService historyService;
//	  protected IdentityService identityService;
//	  protected ManagementService managementService;
//
//  /**
//   * The test process has an XOR gateway where, the 'input' variable is used to
//   * select one of the outgoing sequence flow. Every one of those sequence flow
//   * goes to another task, allowing us to test the decision very easily.
//   */
//  @Deployment
//  @Test
//  public void testDecisionFunctionality() {
//
//  	deploymentIdFromDeploymentAnnotation = TestHelper.annotationDeploymentSetUp(processEngine, getClass(), "testDecisionFunctionality"); 
//  	if (repositoryService==null) {
//        initializeServices();
//      }
//	  
//    Map<String, Object> variables = new HashMap<String, Object>();
//
//    // Test with input == 1
//    variables.put("input", 1);
//    ProcessInstance pi = runtimeService.startProcessInstanceByKey("exclusiveGateway", variables);
//    Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
//    Assert.assertEquals("Send e-mail for more information", task.getName());
//
//    taskService.complete(task.getId());
//    
////    // Test with input == 2
////    variables.put("input", 2);
////    pi = runtimeService.startProcessInstanceByKey("exclusiveGateway", variables);
////    task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
////    assertEquals("Check account balance", task.getName());
////
////    // Test with input == 3
////    variables.put("input", 3);
////    pi = runtimeService.startProcessInstanceByKey("exclusiveGateway", variables);
////    task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
////    assertEquals("Call customer", task.getName());
////
////    // Test with input == 4
////    variables.put("input", 4);
////    try {
////      runtimeService.startProcessInstanceByKey("exclusiveGateway", variables);
////      fail();
////    } catch (ActivitiException e) {
////      // Exception is expected since no outgoing sequence flow matches
////    }
//
//    
//    ProcessEngines.destroy();
//  }
//  protected void initializeServices() {
//	    processEngineConfiguration = ((ProcessEngineImpl) processEngine).getProcessEngineConfiguration();
//	    repositoryService = processEngine.getRepositoryService();
//	    runtimeService = processEngine.getRuntimeService();
//	    taskService = processEngine.getTaskService();
//	    formService = processEngine.getFormService();
//	    historyService = processEngine.getHistoryService();
//	    identityService = processEngine.getIdentityService();
//	    managementService = processEngine.getManagementService();
//	  }
//	  
//}
