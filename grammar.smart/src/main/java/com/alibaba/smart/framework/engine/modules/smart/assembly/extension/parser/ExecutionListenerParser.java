package com.alibaba.smart.framework.engine.modules.smart.assembly.extension.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.Performable;
import com.alibaba.smart.framework.engine.modules.smart.assembly.extension.ExecutionListener;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class ExecutionListenerParser extends AbstractElementParser<ExecutionListener>
       {

    public ExecutionListenerParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    protected ExecutionListener parseModel(XMLStreamReader reader, ParseContext context) {
        ExecutionListener executionListener = new ExecutionListener();
        String event = XmlParseUtil.getString(reader, "event");
        if (null != event) {
            String[] events = event.split(",");
            executionListener.setEvents(events);
        }
        return executionListener;
    }

    @Override
    protected void parseSingleChild(ExecutionListener model, BaseElement child) {
        if (child instanceof Performable) {
            model.setPerformable((Performable)child);
        }
    }

    @Override
    public QName getQname() {
        return ExecutionListener.type;
    }

    @Override
    public Class<ExecutionListener> getModelType() {
        return ExecutionListener.class;
    }

}
