package com.alibaba.smart.framework.engine.ecology.designer.converter;

import com.alibaba.smart.framework.engine.ecology.designer.bpmn.*;
import com.alibaba.smart.framework.engine.ecology.designer.bpmn.Process;
import com.alibaba.smart.framework.engine.ecology.designer.element.bean.*;

/**
 * ProcessFlowModel到BpmnModel的转换器
 */
public class BpmnModelConverter {
    
    public BpmnModel convert(ProcessFlowModel flowModel) {
        BpmnModel bpmnModel = new BpmnModel();
        
        // 创建 Definitions
        Definitions definitions = createDefinitions(flowModel);
        
        // 创建 Process
        Process process = createProcess(flowModel);
        
        definitions.setProcess(process);
        bpmnModel.setDefinitions(definitions);
        
        return bpmnModel;
    }
    
    private Definitions createDefinitions(ProcessFlowModel flowModel) {
        Definitions definitions = new Definitions();
        definitions.setId(flowModel.getKey() + "_def");
        definitions.setVersion(flowModel.getVersion() != null ? flowModel.getVersion().toString() : "1");
        definitions.setTargetNamespace("Examples");
        return definitions;
    }
    
    private Process createProcess(ProcessFlowModel flowModel) {
        Process process = new Process();
        process.setId(flowModel.getKey());
        process.setVersion(flowModel.getVersion() != null ? flowModel.getVersion().toString() : "1");
        process.setName(flowModel.getName());
        
        // 转换节点
        if (flowModel.getNodes() != null) {
            for (FlowNode node : flowModel.getNodes()) {
                BpmnElement element = convertNode(node);
                if (element != null) {
                    process.getElements().add(element);
                }
            }
        }
        
        // 转换边
        if (flowModel.getEdges() != null) {
            for (FlowEdge edge : flowModel.getEdges()) {
                SequenceFlow flow = convertEdge(edge);
                if (flow != null) {
                    process.getElements().add(flow);
                }
            }
        }
        
        return process;
    }
    
    private BpmnElement convertNode(FlowNode node) {
        String type = node.getType();
        
        // 创建扩展元素，保存 position 信息
        ExtensionElements extensionElements = createExtensionElements(node);
        
        switch (type) {
            case "startEvent":
                return convertStartEvent(node, extensionElements);
            case "endEvent":
                return convertEndEvent(node, extensionElements);
            case "userTask":
                return convertUserTask(node, extensionElements);
            case "serviceTask":
                return convertServiceTask(node, extensionElements);
            case "receiveTask":
                return convertReceiveTask(node, extensionElements);
            case "exclusiveGateway":
                return convertExclusiveGateway(node, extensionElements);
            case "parallelGateway":
                return convertParallelGateway(node, extensionElements);
            case "subProcess":
                return convertSubProcess(node, extensionElements);
            default:
                System.err.println("Unknown node type: " + type);
                return null;
        }
    }
    
    private ExtensionElements createExtensionElements(FlowNode node) {
        ExtensionElements ext = new ExtensionElements();
        if (node.getPosition() != null) {
            ext.addProperty("x", node.getPosition().getX());
            ext.addProperty("y", node.getPosition().getY());
        }
        return ext;
    }
    
    private StartEvent convertStartEvent(FlowNode node, ExtensionElements ext) {
        StartEvent event = new StartEvent();
        event.setId(node.getId());
        if (node.getData() != null && node.getData().getConfig() != null) {
            event.setName(node.getData().getConfig().getName());
        }
        event.setExtensionElements(ext);
        return event;
    }
    
    private EndEvent convertEndEvent(FlowNode node, ExtensionElements ext) {
        EndEvent event = new EndEvent();
        event.setId(node.getId());
        if (node.getData() != null && node.getData().getConfig() != null) {
            event.setName(node.getData().getConfig().getName());
            event.setTerminateAll(node.getData().getConfig().getTerminateAll());
        }
        event.setExtensionElements(ext);
        return event;
    }
    
    private UserTask convertUserTask(FlowNode node, ExtensionElements ext) {
        UserTask task = new UserTask();
        task.setId(node.getId());
        
        if (node.getData() != null) {
            task.setName(node.getData().getLabel());
            
            if (node.getData().getConfig() != null) {
                NodeConfig config = node.getData().getConfig();
                task.setPriority(config.getPriority());
                task.setSmartClass(config.getSmartClass());
                
                // 处理assignee配置
                if (config.getAssignee() != null && config.getAssignee().getValue() != null) {
                    task.setAssignee(config.getAssignee().getValue());
                }
            }
        }
        
        task.setExtensionElements(ext);
        return task;
    }
    
    private ServiceTask convertServiceTask(FlowNode node, ExtensionElements ext) {
        ServiceTask task = new ServiceTask();
        task.setId(node.getId());
        
        if (node.getData() != null) {
            task.setName(node.getData().getLabel());
            
            if (node.getData().getConfig() != null) {
                task.setSmartClass(node.getData().getConfig().getSmartClass());
            }
        }
        
        task.setExtensionElements(ext);
        return task;
    }
    
    private ReceiveTask convertReceiveTask(FlowNode node, ExtensionElements ext) {
        ReceiveTask task = new ReceiveTask();
        task.setId(node.getId());
        
        if (node.getData() != null) {
            task.setName(node.getData().getLabel());
        }
        
        task.setExtensionElements(ext);
        return task;
    }
    
    private ExclusiveGateway convertExclusiveGateway(FlowNode node, ExtensionElements ext) {
        ExclusiveGateway gateway = new ExclusiveGateway();
        gateway.setId(node.getId());
        
        if (node.getData() != null) {
            gateway.setName(node.getData().getLabel());
        }
        
        gateway.setExtensionElements(ext);
        return gateway;
    }
    
    private ParallelGateway convertParallelGateway(FlowNode node, ExtensionElements ext) {
        ParallelGateway gateway = new ParallelGateway();
        gateway.setId(node.getId());
        
        if (node.getData() != null) {
            gateway.setName(node.getData().getLabel());
        }
        
        gateway.setExtensionElements(ext);
        return gateway;
    }
    
    private SubProcess convertSubProcess(FlowNode node, ExtensionElements ext) {
        SubProcess subProcess = new SubProcess();
        subProcess.setId(node.getId());
        
        if (node.getData() != null) {
            subProcess.setName(node.getData().getLabel());
        }
        
        subProcess.setExtensionElements(ext);
        return subProcess;
    }
    
    private SequenceFlow convertEdge(FlowEdge edge) {
        SequenceFlow flow = new SequenceFlow();
        flow.setId(edge.getId());
        flow.setSourceRef(edge.getSource());
        flow.setTargetRef(edge.getTarget());
        
        if (edge.getData() != null) {
            flow.setName(edge.getData().getLabel());
            
            // 处理条件表达式
            if (edge.getData().getCondition() != null) {
                ConditionConfig condition = edge.getData().getCondition();
                ConditionExpression expression = new ConditionExpression();
                expression.setType("mvel");
                expression.setContent(condition.getContent());
                flow.setConditionExpression(expression);
            }
        }
        
        return flow;
    }
}
