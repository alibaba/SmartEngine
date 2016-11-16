package com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.parser;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ReceiveTask;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class ReceiveTaskParser extends AbstractBpmnActivityParser<ReceiveTask> implements StAXArtifactParser<ReceiveTask> {

    public ReceiveTaskParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        return ReceiveTask.type;
    }

    @Override
    public Class<ReceiveTask> getModelType() {
        return ReceiveTask.class;
    }

    @Override
    public ReceiveTask parse(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        ReceiveTask receiveTask = new ReceiveTask();
        receiveTask.setId(this.getString(reader, "id"));

        //no need add namespaceUri ,or using reader.getAttributeValue(2) (not good),see http://www.saxproject.org/namespaces.html,
        // http://stackoverflow.com/questions/7157355/what-is-the-difference-between-localname-and-qname,http://stackoverflow.com/questions/6390339/how-to-query-xml-using-namespaces-in-java-with-xpath
        String className = this.getString(reader, "class");
        receiveTask.setClassName(className);
        this.parseChildren(receiveTask, reader, context);
        return receiveTask;
    }

    @Override
    protected void parseModelChild(ReceiveTask model, BaseElement child) {
//        if (child instanceof Action) {
//            model.setAction((Action) child);
//        }
//        if (child instanceof ProcessEvents) {
//            model.setEvents((ProcessEvents) child);
//        }

    }


}
