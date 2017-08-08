package com.alibaba.smart.framework.engine.xml.parser.impl;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public abstract class AbstractElementParser<M extends BaseElement> extends AbstractStAXArtifactParser<M> implements StAXArtifactParser<M>{

    public AbstractElementParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }



    @Override
    public M parse(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        M model=this.parseModel(reader,context);
        this.parseAttribute(model,reader,context);
        this.parseChildren(model,reader,context);
        return model;
    }

    protected void parseAttribute(M model, XMLStreamReader reader, ParseContext context) throws ParseException,XMLStreamException{
        int attributeCount=reader.getAttributeCount();
        if(attributeCount>0){
            for (int i = 0; i < attributeCount; i++) {
                QName attributeName=reader.getAttributeName(i);
                Object value=this.readAttribute(attributeName,reader, context);
                if(null!=value && value instanceof BaseElement){
                    this.parseChild(model, (BaseElement) value);
                }
            }
        }
    }


    protected void parseChildren(M model, XMLStreamReader reader, ParseContext context) throws ParseException,
        XMLStreamException {
        while (this.nextChildElement(reader)) {
            Object element = this.readElement(reader, context);
            if (element instanceof BaseElement) {
                this.parseChild(model, (BaseElement) element);
            }
        }
    }

    protected void parseChild(M model, BaseElement child) throws ParseException{

    }


    protected abstract M parseModel(XMLStreamReader reader, ParseContext context) throws ParseException,XMLStreamException;



}
