package com.alibaba.smart.framework.engine.ecology.designer;

import com.alibaba.smart.framework.engine.ecology.designer.converter.JsonToBpmnConverter;
import com.alibaba.smart.framework.engine.ecology.designer.element.bean.*;
import com.alibaba.smart.framework.engine.util.IOUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON到BPMN转换器测试
 */
public class JsonToBpmnConverterTest {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Test
    public void testConvertProcessJson() throws IOException {
        // 读取JSON文件
        String jsonPath = "src/test/resources/json-to-bpmn/process.json";
        String jsonContent = readFile(jsonPath);
        
        // 使用Jackson解析JSON
        ProcessFlowModel flowModel = objectMapper.readValue(jsonContent, ProcessFlowModel.class);
        
        // 验证解析结果
        Assert.assertNotNull(flowModel);
        Assert.assertEquals("新建流程", flowModel.getName());
        Assert.assertEquals("process_1763379016790", flowModel.getKey());
        Assert.assertEquals(Integer.valueOf(1), flowModel.getVersion());
        Assert.assertEquals(5, flowModel.getNodes().size());
        Assert.assertEquals(5, flowModel.getEdges().size());
        
        // 转换为BPMN XML
        JsonToBpmnConverter converter = new JsonToBpmnConverter();
        String bpmnXml = converter.convert(flowModel);
        
        // 验证XML生成
        Assert.assertNotNull(bpmnXml);
        Assert.assertTrue(bpmnXml.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        Assert.assertTrue(bpmnXml.contains("<definitions"));
        Assert.assertTrue(bpmnXml.contains("<process"));
        Assert.assertTrue(bpmnXml.contains("id=\"process_1763379016790\""));
        Assert.assertTrue(bpmnXml.contains("name=\"新建流程\""));
        
        // 验证节点
        Assert.assertTrue(bpmnXml.contains("<startEvent"));
        Assert.assertTrue(bpmnXml.contains("id=\"startEvent-1763379018399\""));
        Assert.assertTrue(bpmnXml.contains("<endEvent"));
        Assert.assertTrue(bpmnXml.contains("id=\"endEvent-1763379036897\""));
        Assert.assertTrue(bpmnXml.contains("<userTask"));
        Assert.assertTrue(bpmnXml.contains("id=\"userTask-1763379019795\""));
        Assert.assertTrue(bpmnXml.contains("name=\"员工申请\""));
        
        // 验证连线
        Assert.assertTrue(bpmnXml.contains("<sequenceFlow"));
        Assert.assertTrue(bpmnXml.contains("sourceRef=\"startEvent-1763379018399\""));
        Assert.assertTrue(bpmnXml.contains("targetRef=\"userTask-1763379019795\""));
        
        // 验证条件表达式
        Assert.assertTrue(bpmnXml.contains("<conditionExpression"));
        Assert.assertTrue(bpmnXml.contains("leaveDays < 3") || bpmnXml.contains("leaveDays &lt; 3"));
        Assert.assertTrue(bpmnXml.contains("leaveDays >= 3") || bpmnXml.contains("leaveDays &gt;= 3"));
        
        // 验证扩展元素（position）
        Assert.assertTrue(bpmnXml.contains("<extensionElements>"));
        Assert.assertTrue(bpmnXml.contains("position"));
        
        // 打印生成的XML（用于调试）
        System.out.println("Generated BPMN XML:");
        System.out.println(bpmnXml);
        
//        // 保存到文件
//        String outputPath = "target/test-output-process.bpmn20.xml";
//        Files.createDirectories(Paths.get("target"));
//        Files.writeString(Paths.get(outputPath), bpmnXml, StandardCharsets.UTF_8);
//        System.out.println("\nBPMN XML saved to: " + outputPath);
    }
    
    @Test
    public void testConvertFromJsonString() throws IOException {
        // 读取JSON文件
        String jsonPath = "json-to-bpmn/process.json";
        String jsonContent = readFile(jsonPath);
        
        // 直接从JSON字符串转换
        JsonToBpmnConverter converter = new JsonToBpmnConverter();
        String bpmnXml = converter.convert(jsonContent);
        
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
        JsonToBpmnConverter converter = new JsonToBpmnConverter();
        String bpmnXml = converter.convert(flowModel);
        
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
        JsonToBpmnConverter converter = new JsonToBpmnConverter();
        String bpmnXml = converter.convert(flowModel);
        
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
        JsonToBpmnConverter converter = new JsonToBpmnConverter();
        String bpmnXml = converter.convert(flowModel);
        
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
        model.setVersion(1);
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
        model.setVersion(1);
        
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
        model.setVersion(1);
        
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
