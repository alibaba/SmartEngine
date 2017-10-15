package com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.CollectionCondition;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.CompletionCondition;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.MultiInstanceLoopCharacteristics;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.impl.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.impl.AbstractStAXArtifactParser;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  21:01.
 */
public class MultiInstanceLoopCharacteristicsParser extends AbstractElementParser<MultiInstanceLoopCharacteristics>
    implements StAXArtifactParser<MultiInstanceLoopCharacteristics> {

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
    protected MultiInstanceLoopCharacteristics parseModel(XMLStreamReader reader, ParseContext context)
        throws ParseException, XMLStreamException {
        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = new MultiInstanceLoopCharacteristics();
        Boolean isSequential=this.getBoolean(reader,"isSequential");
        if(null!=isSequential){
            multiInstanceLoopCharacteristics.setSequential(isSequential);
        }else{
            multiInstanceLoopCharacteristics.setSequential(false);
        }
        multiInstanceLoopCharacteristics.setElementVariable(this.getString(reader,"elementVariable"));

        return multiInstanceLoopCharacteristics;
    }

    @Override
    protected void parseChild(MultiInstanceLoopCharacteristics model, BaseElement child) throws ParseException {
        if (child instanceof CollectionCondition) {
            model.setCollectionCondition((CollectionCondition)child);
        } else if (child instanceof CompletionCondition) {
            model.setCompletionCondition((CompletionCondition)child);
        } else {
            throw  new EngineException("Should be a instance of CompletionCondition :"+child.getClass());
        }
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