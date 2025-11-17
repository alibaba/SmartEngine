package com.alibaba.smart.framework.engine.ecology.designer.converter;

import com.alibaba.smart.framework.engine.ecology.designer.bpmn.*;
import com.alibaba.smart.framework.engine.ecology.designer.bpmn.Process;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.StringWriter;
import java.util.Map;

/**
 * BPMN XML生成器 - 使用DOM4J
 */
public class BpmnXmlGenerator {
    
    public String generate(BpmnModel bpmnModel) {
        try {
            Document document = DocumentHelper.createDocument();
            
            // 创建根元素 definitions
            Element definitions = createDefinitions(document, bpmnModel.getDefinitions());
            document.setRootElement(definitions);
            
            // 创建 process 元素
            if (bpmnModel.getDefinitions().getProcess() != null) {
                createProcess(definitions, bpmnModel.getDefinitions().getProcess());
            }
            
            // 格式化输出
            return formatXml(document);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate BPMN XML", e);
        }
    }
    
    private Element createDefinitions(Document doc, Definitions def) {
        // 创建根元素
        Element element = doc.addElement("definitions");
        
        // 添加命名空间
        for (Map.Entry<String, String> ns : def.getNamespaces().entrySet()) {
            String key = ns.getKey();
            String uri = ns.getValue();
            
            if ("xmlns".equals(key)) {
                // 默认命名空间
                element.addNamespace("", uri);
            } else if (key.startsWith("xmlns:")) {
                // 带前缀的命名空间
                String prefix = key.substring(6);
                element.addNamespace(prefix, uri);
            }
        }
        
        // 添加属性
        element.addAttribute("id", def.getId());
        if (def.getVersion() != null) {
            element.addAttribute("version", def.getVersion());
        }
        element.addAttribute("targetNamespace", def.getTargetNamespace());
        
        return element;
    }
    
    private Element createProcess(Element parent, Process process) {
        Element element = parent.addElement("process");
        
        element.addAttribute("id", process.getId());
        if (process.getVersion() != null) {
            element.addAttribute("version", process.getVersion());
        }
        if (process.getName() != null) {
            element.addAttribute("name", process.getName());
        }
        
        // 添加所有子元素
        for (BpmnElement bpmnElement : process.getElements()) {
            createBpmnElement(element, bpmnElement);
        }
        
        return element;
    }
    
    private void createBpmnElement(Element parent, BpmnElement bpmnElement) {
        String tagName = bpmnElement.getElementType();
        Element element = parent.addElement(tagName);
        
        // 添加属性
        Map<String, String> attrs = bpmnElement.getAttributes();
        for (Map.Entry<String, String> attr : attrs.entrySet()) {
            String attrName = attr.getKey();
            String attrValue = attr.getValue();
            
            if (attrValue != null) {
                // 处理带命名空间的属性（如 smart:class）
                if (attrName.contains(":")) {
                    String[] parts = attrName.split(":");
                    String prefix = parts[0];
                    String localName = parts[1];
                    
                    // 获取命名空间URI
                    Namespace ns = element.getNamespaceForPrefix(prefix);
                    if (ns != null) {
                        element.addAttribute(new QName(localName, ns), attrValue);
                    } else {
                        element.addAttribute(attrName, attrValue);
                    }
                } else {
                    element.addAttribute(attrName, attrValue);
                }
            }
        }
        
        // 添加扩展元素
        if (bpmnElement.getExtensionElements() != null && bpmnElement.getExtensionElements().hasProperties()) {
            createExtensionElements(element, bpmnElement.getExtensionElements());
        }
        
        // 添加条件表达式
        if (bpmnElement instanceof SequenceFlow) {
            SequenceFlow flow = (SequenceFlow) bpmnElement;
            if (flow.getConditionExpression() != null) {
                createConditionExpression(element, flow.getConditionExpression());
            }
        }
        
        // 添加子元素
        for (BpmnElement child : bpmnElement.getChildren()) {
            createBpmnElement(element, child);
        }
    }
    
    private void createExtensionElements(Element parent, ExtensionElements ext) {
        Element extElement = parent.addElement("extensionElements");
        
        // 添加 position 等自定义属性
        if (ext.getProperty("x") != null && ext.getProperty("y") != null) {
            Namespace smartNs = parent.getNamespaceForPrefix("smart");
            Element position = extElement.addElement(new QName("position", smartNs));
            position.addAttribute("x", ext.getProperty("x").toString());
            position.addAttribute("y", ext.getProperty("y").toString());
        }
    }
    
    private void createConditionExpression(Element parent, ConditionExpression expression) {
        Element condElement = parent.addElement("conditionExpression");
        
        if (expression.getType() != null) {
            Namespace xsiNs = parent.getNamespaceForPrefix("xsi");
            if (xsiNs != null) {
                condElement.addAttribute(new QName("type", xsiNs), expression.getType());
            }
        }
        
        if (expression.getContent() != null) {
            condElement.setText(expression.getContent());
        }
    }
    
    private String formatXml(Document document) {
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            format.setIndent(true);
            format.setIndentSize(4);
            format.setNewlines(true);
            format.setTrimText(false);
            
            StringWriter writer = new StringWriter();
            XMLWriter xmlWriter = new XMLWriter(writer, format);
            xmlWriter.write(document);
            xmlWriter.close();
            
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to format XML", e);
        }
    }
}
