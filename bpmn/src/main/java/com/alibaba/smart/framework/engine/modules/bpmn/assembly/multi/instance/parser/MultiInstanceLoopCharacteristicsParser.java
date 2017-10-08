package com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.CompletionCondition;
import com.alibaba.smart.framework.engine.model.assembly.Extension;
import com.alibaba.smart.framework.engine.model.assembly.MultiInstanceLoopCharacteristics;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.extension.ExtensionElements;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.UserTask;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.impl.AbstractStAXArtifactParser;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  21:01.
 */
public class MultiInstanceLoopCharacteristicsParser extends AbstractStAXArtifactParser<MultiInstanceLoopCharacteristics> implements StAXArtifactParser<MultiInstanceLoopCharacteristics> {

    public MultiInstanceLoopCharacteristicsParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }



    @Override
    public QName getArtifactType() {
        return MultiInstanceLoopCharacteristics.type;
    }

    @Override
    public Class<MultiInstanceLoopCharacteristics> getModelType() {
        return MultiInstanceLoopCharacteristics.class;
    }

    @Override
    public MultiInstanceLoopCharacteristics parse(XMLStreamReader reader, ParseContext context)
        throws ParseException, XMLStreamException {
        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = new MultiInstanceLoopCharacteristics();


        while (this.nextChildElement(reader)) {
            Object element = this.readElement(reader, context);
            if (element instanceof CompletionCondition) {
                multiInstanceLoopCharacteristics.setCompletionCondition((CompletionCondition)element);
            } else {
                throw  new EngineException("Should be a instance of CompletionCondition :"+element.getClass());
            }
        }

        return multiInstanceLoopCharacteristics;
    }
}