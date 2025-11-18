package com.alibaba.smart.framework.engine.ecology.designer;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.bpmn.assembly.event.StartEvent;
import com.alibaba.smart.framework.engine.comments.parser.CommentsTest;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultIdGenerator;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.ecology.designer.converter.JsonToBpmnConverter;
import com.alibaba.smart.framework.engine.ecology.designer.element.bean.*;
import com.alibaba.smart.framework.engine.extension.scanner.SimpleAnnotationScanner;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinitionSource;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.util.IOUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON到BPMN转换器测试
 */
public class JsonToBpmnConverterTest {

    private SmartEngine smartEngine;

    private final ObjectMapper objectMapper = new ObjectMapper();


    private RepositoryCommandService repositoryCommandService;

    @Before
    public void initEngine() {
        //1.初始化
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        processEngineConfiguration.setIdGenerator(new DefaultIdGenerator());

        processEngineConfiguration.setAnnotationScanner(
                new SimpleAnnotationScanner(
                        SmartEngine.class.getPackage().getName(),
                        CommentsTest.class.getPackage().getName()));
        smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        //2.获得常用服务
        repositoryCommandService = smartEngine.getRepositoryCommandService();

    }
    

    @Test
    public void testConvertProcessJson() throws IOException {
        // 读取JSON文件
        String jsonPath = "json-to-bpmn/process.json";
        String jsonContent = readFile(jsonPath);
        
        // 使用Jackson解析JSON
        ProcessFlowModel flowModel = objectMapper.readValue(jsonContent, ProcessFlowModel.class);
        
        // 验证解析结果
        Assert.assertNotNull(flowModel);
        Assert.assertEquals("新建流程", flowModel.getName());
        Assert.assertEquals("process_1763379016790", flowModel.getKey());
        Assert.assertEquals("1.0.0", flowModel.getVersion());
        Assert.assertEquals(5, flowModel.getNodes().size());
        Assert.assertEquals(5, flowModel.getEdges().size());
        
        // 转换为BPMN XML
        String bpmnXml = JsonToBpmnConverter.convert(flowModel);
        System.out.println(bpmnXml);

        ProcessDefinitionSource processDefinitionSource = repositoryCommandService.deployWithUTF8Content(bpmnXml);
        ProcessDefinition firstProcessDefinition = processDefinitionSource.getFirstProcessDefinition();


       Assert.assertEquals("process_1763379016790",firstProcessDefinition.getId());
        Assert.assertEquals("新建流程",firstProcessDefinition.getName());
        Assert.assertEquals("1.0.0",firstProcessDefinition.getVersion());

        // 验证节点 - 使用从ProcessDefinition获取元素的方式

        // 验证开始事件
        String startEventId = "startEvent-1763379018399";
        com.alibaba.smart.framework.engine.bpmn.assembly.event.StartEvent startEvent = 
            (com.alibaba.smart.framework.engine.bpmn.assembly.event.StartEvent) firstProcessDefinition.getIdBasedElementMap().get(startEventId);
        Assert.assertNotNull(startEvent);
        Assert.assertEquals(startEventId, startEvent.getId());
        Assert.assertEquals("开始", startEvent.getName());

        // 验证结束事件
        String endEventId = "endEvent-1763379036897";
        com.alibaba.smart.framework.engine.bpmn.assembly.event.EndEvent endEvent = 
            (com.alibaba.smart.framework.engine.bpmn.assembly.event.EndEvent) firstProcessDefinition.getIdBasedElementMap().get(endEventId);
        Assert.assertNotNull(endEvent);
        Assert.assertEquals(endEventId, endEvent.getId());
        Assert.assertEquals("结束", endEvent.getName());

        // 验证用户任务1 - 员工申请
        String userTask1Id = "userTask-1763379019795";
        com.alibaba.smart.framework.engine.bpmn.assembly.task.UserTask userTask1 = 
            (com.alibaba.smart.framework.engine.bpmn.assembly.task.UserTask) firstProcessDefinition.getIdBasedElementMap().get(userTask1Id);
        Assert.assertNotNull(userTask1);
        Assert.assertEquals(userTask1Id, userTask1.getId());
        Assert.assertEquals("员工申请", userTask1.getName());

        // 验证用户任务2 - 领导审批
        String userTask2Id = "userTask-1763379023480";
        com.alibaba.smart.framework.engine.bpmn.assembly.task.UserTask userTask2 = 
            (com.alibaba.smart.framework.engine.bpmn.assembly.task.UserTask) firstProcessDefinition.getIdBasedElementMap().get(userTask2Id);
        Assert.assertNotNull(userTask2);
        Assert.assertEquals(userTask2Id, userTask2.getId());
        Assert.assertEquals("领导审批", userTask2.getName());

        // 验证用户任务3 - HR审批
        String userTask3Id = "userTask-1763379027894";
        com.alibaba.smart.framework.engine.bpmn.assembly.task.UserTask userTask3 = 
            (com.alibaba.smart.framework.engine.bpmn.assembly.task.UserTask) firstProcessDefinition.getIdBasedElementMap().get(userTask3Id);
        Assert.assertNotNull(userTask3);
        Assert.assertEquals(userTask3Id, userTask3.getId());
        Assert.assertEquals("HR审批", userTask3.getName());
        
        // 验证连线 - 从开始到员工申请
        String flow1Id = "edge-1763379021762";
        com.alibaba.smart.framework.engine.bpmn.assembly.process.SequenceFlow flow1 = 
            (com.alibaba.smart.framework.engine.bpmn.assembly.process.SequenceFlow) firstProcessDefinition.getIdBasedElementMap().get(flow1Id);
        Assert.assertNotNull(flow1);
        Assert.assertEquals(flow1Id, flow1.getId());
        Assert.assertEquals(startEventId, flow1.getSourceRef());
        Assert.assertEquals(userTask1Id, flow1.getTargetRef());
        
        // 验证条件连线1 - 请假天数< 3天
        String flow2Id = "edge-1763379025162";
        com.alibaba.smart.framework.engine.bpmn.assembly.process.SequenceFlow flow2 = 
            (com.alibaba.smart.framework.engine.bpmn.assembly.process.SequenceFlow) firstProcessDefinition.getIdBasedElementMap().get(flow2Id);
        Assert.assertNotNull(flow2);
        Assert.assertEquals(flow2Id, flow2.getId());
        Assert.assertEquals(userTask1Id, flow2.getSourceRef());
        Assert.assertEquals(userTask2Id, flow2.getTargetRef());
        Assert.assertEquals("请假天数< 3天", flow2.getName());
        Assert.assertNotNull(flow2.getConditionExpression());
        Assert.assertEquals("leaveDays < 3", flow2.getConditionExpression().getExpressionContent());
        
        // 验证条件连线2 - 请假天数 >=3天
        String flow3Id = "edge-1763379030029";
        com.alibaba.smart.framework.engine.bpmn.assembly.process.SequenceFlow flow3 = 
            (com.alibaba.smart.framework.engine.bpmn.assembly.process.SequenceFlow) firstProcessDefinition.getIdBasedElementMap().get(flow3Id);
        Assert.assertNotNull(flow3);
        Assert.assertEquals(flow3Id, flow3.getId());
        Assert.assertEquals(userTask1Id, flow3.getSourceRef());
        Assert.assertEquals(userTask3Id, flow3.getTargetRef());
        Assert.assertEquals("请假天数 >=3天", flow3.getName());
        Assert.assertNotNull(flow3.getConditionExpression());
        Assert.assertEquals("leaveDays >=3", flow3.getConditionExpression().getExpressionContent());

        // 验证连线 - 领导审批到结束
        String flow4Id = "edge-1763379038895";
        com.alibaba.smart.framework.engine.bpmn.assembly.process.SequenceFlow flow4 = 
            (com.alibaba.smart.framework.engine.bpmn.assembly.process.SequenceFlow) firstProcessDefinition.getIdBasedElementMap().get(flow4Id);
        Assert.assertNotNull(flow4);
        Assert.assertEquals(flow4Id, flow4.getId());
        Assert.assertEquals(userTask2Id, flow4.getSourceRef());
        Assert.assertEquals(endEventId, flow4.getTargetRef());

        // 验证连线 - HR审批到结束
        String flow5Id = "edge-1763379041329";
        com.alibaba.smart.framework.engine.bpmn.assembly.process.SequenceFlow flow5 = 
            (com.alibaba.smart.framework.engine.bpmn.assembly.process.SequenceFlow) firstProcessDefinition.getIdBasedElementMap().get(flow5Id);
        Assert.assertNotNull(flow5);
        Assert.assertEquals(flow5Id, flow5.getId());
        Assert.assertEquals(userTask3Id, flow5.getSourceRef());
        Assert.assertEquals(endEventId, flow5.getTargetRef());
        
        // 打印生成的XML（用于调试）
        System.out.println("Generated BPMN XML:");
        System.out.println(bpmnXml);
        

    }
    
    @Test
    public void testConvertFromJsonString() throws IOException {
        // 读取JSON文件
        String jsonPath = "json-to-bpmn/process.json";
        String jsonContent = readFile(jsonPath);
        
        // 直接从JSON字符串转换
        String bpmnXml = JsonToBpmnConverter.convert(jsonContent);

        // 验证
        Assert.assertNotNull(bpmnXml);
        Assert.assertTrue(bpmnXml.contains("process_1763379016790"));
        Assert.assertTrue(bpmnXml.contains("新建流程"));
        
        System.out.println("\nDirect JSON String Conversion:");
        System.out.println(bpmnXml);
    }
    
    @Test
    public void testConvertSimpleProcess() {
        // 创建简单的流程模型
        ProcessFlowModel flowModel = createSimpleFlowModel();
        
        // 转换
        String bpmnXml = JsonToBpmnConverter.convert(flowModel);
        
        // 验证
        Assert.assertNotNull(bpmnXml);
        Assert.assertTrue(bpmnXml.contains("id=\"simple-process\""));
        Assert.assertTrue(bpmnXml.contains("name=\"简单流程\""));
        Assert.assertTrue(bpmnXml.contains("<startEvent"));
        Assert.assertTrue(bpmnXml.contains("<endEvent"));
        Assert.assertTrue(bpmnXml.contains("<sequenceFlow"));
        
        System.out.println("\nSimple Process BPMN XML:");
        System.out.println(bpmnXml);
    }
    
    @Test
    public void testConvertWithGateways() {
        // 创建包含网关的流程模型
        ProcessFlowModel flowModel = createGatewayFlowModel();
        
        // 转换
        String bpmnXml = JsonToBpmnConverter.convert(flowModel);
        
        // 验证
        Assert.assertNotNull(bpmnXml);
        Assert.assertTrue(bpmnXml.contains("<exclusiveGateway"));
        Assert.assertTrue(bpmnXml.contains("<parallelGateway"));
        
        System.out.println("\nGateway Process BPMN XML:");
        System.out.println(bpmnXml);
    }
    
    @Test
    public void testConvertWithServiceTask() {
        // 创建包含服务任务的流程
        ProcessFlowModel flowModel = createServiceTaskFlowModel();
        
        // 转换
        String bpmnXml = JsonToBpmnConverter.convert(flowModel);
        
        // 验证
        Assert.assertNotNull(bpmnXml);
        Assert.assertTrue(bpmnXml.contains("<serviceTask"));
        Assert.assertTrue(bpmnXml.contains("smart:class"));
        
        System.out.println("\nService Task Process BPMN XML:");
        System.out.println(bpmnXml);
    }
    
    private ProcessFlowModel createSimpleFlowModel() {
        ProcessFlowModel model = new ProcessFlowModel();
        model.setName("简单流程");
        model.setKey("simple-process");
        model.setVersion("1.0.0");
        model.setStatus("draft");
        
        List<FlowNode> nodes = new ArrayList<>();
        List<FlowEdge> edges = new ArrayList<>();
        
        // 开始节点
        FlowNode startNode = new FlowNode();
        startNode.setId("start-1");
        startNode.setType("startEvent");
        Position startPos = new Position();
        startPos.setX(100.0);
        startPos.setY(100.0);
        startNode.setPosition(startPos);
        NodeData startData = new NodeData();
        startData.setType("startEvent");
        startData.setLabel("开始");
        NodeConfig startConfig = new NodeConfig();
        startConfig.setName("开始");
        startData.setConfig(startConfig);
        startNode.setData(startData);
        nodes.add(startNode);
        
        // 结束节点
        FlowNode endNode = new FlowNode();
        endNode.setId("end-1");
        endNode.setType("endEvent");
        Position endPos = new Position();
        endPos.setX(300.0);
        endPos.setY(100.0);
        endNode.setPosition(endPos);
        NodeData endData = new NodeData();
        endData.setType("endEvent");
        endData.setLabel("结束");
        NodeConfig endConfig = new NodeConfig();
        endConfig.setName("结束");
        endData.setConfig(endConfig);
        endNode.setData(endData);
        nodes.add(endNode);
        
        // 连线
        FlowEdge edge = new FlowEdge();
        edge.setId("flow-1");
        edge.setSource("start-1");
        edge.setTarget("end-1");
        edge.setType("smoothstep");
        EdgeData edgeData = new EdgeData();
        edgeData.setLabel("");
        edge.setData(edgeData);
        edges.add(edge);
        
        model.setNodes(nodes);
        model.setEdges(edges);
        
        return model;
    }
    
    private ProcessFlowModel createGatewayFlowModel() {
        ProcessFlowModel model = new ProcessFlowModel();
        model.setName("网关流程");
        model.setKey("gateway-process");
        model.setVersion("1.0.0");
        
        List<FlowNode> nodes = new ArrayList<>();
        List<FlowEdge> edges = new ArrayList<>();
        
        // 开始节点
        FlowNode startNode = createNode("start-1", "startEvent", "开始", 100.0, 100.0);
        nodes.add(startNode);
        
        // 排他网关
        FlowNode exclusiveGw = createNode("gw-1", "exclusiveGateway", "排他网关", 200.0, 100.0);
        nodes.add(exclusiveGw);
        
        // 并行网关
        FlowNode parallelGw = createNode("gw-2", "parallelGateway", "并行网关", 300.0, 100.0);
        nodes.add(parallelGw);
        
        // 结束节点
        FlowNode endNode = createNode("end-1", "endEvent", "结束", 400.0, 100.0);
        nodes.add(endNode);
        
        // 连线
        edges.add(createEdge("flow-1", "start-1", "gw-1", null));
        edges.add(createEdge("flow-2", "gw-1", "gw-2", null));
        edges.add(createEdge("flow-3", "gw-2", "end-1", null));
        
        model.setNodes(nodes);
        model.setEdges(edges);
        
        return model;
    }
    
    private ProcessFlowModel createServiceTaskFlowModel() {
        ProcessFlowModel model = new ProcessFlowModel();
        model.setName("服务任务流程");
        model.setKey("service-task-process");
        model.setVersion("1.0.0");
        
        List<FlowNode> nodes = new ArrayList<>();
        List<FlowEdge> edges = new ArrayList<>();
        
        // 开始节点
        FlowNode startNode = createNode("start-1", "startEvent", "开始", 100.0, 100.0);
        nodes.add(startNode);
        
        // 服务任务
        FlowNode serviceTask = createNode("service-1", "serviceTask", "服务任务", 200.0, 100.0);
        serviceTask.getData().getConfig().setSmartClass("com.example.MyService");
        nodes.add(serviceTask);
        
        // 结束节点
        FlowNode endNode = createNode("end-1", "endEvent", "结束", 300.0, 100.0);
        nodes.add(endNode);
        
        // 连线
        edges.add(createEdge("flow-1", "start-1", "service-1", null));
        edges.add(createEdge("flow-2", "service-1", "end-1", null));
        
        model.setNodes(nodes);
        model.setEdges(edges);
        
        return model;
    }
    
    private FlowNode createNode(String id, String type, String label, Double x, Double y) {
        FlowNode node = new FlowNode();
        node.setId(id);
        node.setType(type);
        
        Position pos = new Position();
        pos.setX(x);
        pos.setY(y);
        node.setPosition(pos);
        
        NodeData data = new NodeData();
        data.setType(type);
        data.setLabel(label);
        
        NodeConfig config = new NodeConfig();
        config.setName(label);
        data.setConfig(config);
        
        node.setData(data);
        return node;
    }
    
    private FlowEdge createEdge(String id, String source, String target, String condition) {
        FlowEdge edge = new FlowEdge();
        edge.setId(id);
        edge.setSource(source);
        edge.setTarget(target);
        edge.setType("smoothstep");
        
        EdgeData data = new EdgeData();
        data.setLabel("");
        
        if (condition != null) {
            ConditionConfig condConfig = new ConditionConfig();
            condConfig.setType("expression");
            condConfig.setContent(condition);
            data.setCondition(condConfig);
        }
        
        edge.setData(data);
        return edge;
    }
    
    private String readFile(String path) throws IOException {
        return IOUtil.readResourceFileAsUTF8String(path);
    }
}
