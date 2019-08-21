package com.alibaba.smart.framework.engine.xml.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.xml.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public abstract class AbstractElementParser<M extends BaseElement> implements ElementParser<M> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractElementParser.class);

    private ExtensionPointRegistry extensionPointRegistry;
    private XmlParserExtensionPoint xmlParserExtensionPoint;

    // GETTER & SETTER

    protected ExtensionPointRegistry getExtensionPointRegistry() {
        return extensionPointRegistry;
    }

    protected XmlParserExtensionPoint getXmlParserExtensionPoint() {
        return xmlParserExtensionPoint;
    }

    public AbstractElementParser(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void start() {
        this.xmlParserExtensionPoint = this.extensionPointRegistry.getExtensionPoint(XmlParserExtensionPoint.class);
    }

    @Override
    public void stop() {

    }


    protected Object readElement(XMLStreamReader reader, ParseContext context) throws ParseException,
        XMLStreamException {
        XmlParserExtensionPoint xmlParserExtensionPoint = this.getXmlParserExtensionPoint();
        return xmlParserExtensionPoint.parseElement(reader, context);
    }





    @Override
    public M parseElement(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        M model=this.parseModel(reader,context);
        context=context.evolve(model);
        this.parseAttribute(model,reader,context);
        this.parseChildren(model,reader,context);
        return model;
    }


    protected   M parseModel(XMLStreamReader reader, ParseContext context) throws ParseException,XMLStreamException{
       throw  new EngineException("should be overridden");
    }



    private void parseAttribute(M model, XMLStreamReader reader, ParseContext context) throws ParseException,XMLStreamException{
        int attributeCount=reader.getAttributeCount();
        if(attributeCount>0){
            for (int i = 0; i < attributeCount; i++) {
                QName attributeQName=reader.getAttributeName(i);
                Object value=this.readAttribute(attributeQName,reader, context);
                if(null!=value && value instanceof BaseElement){
                    this.parseChild(model, (BaseElement) value);
                }else{
                    LOGGER.debug(attributeQName +" is ignored when parsing attribute from "+ model);
                }
            }
        }
    }

    private Object readAttribute(QName attributeName,XMLStreamReader reader, ParseContext context) throws ParseException,
        XMLStreamException {
        XmlParserExtensionPoint xmlParserExtensionPoint = this.getXmlParserExtensionPoint();
        return xmlParserExtensionPoint.parseAttribute(attributeName,reader, context);
    }


    private void parseChildren(M model, XMLStreamReader reader, ParseContext context) throws ParseException,
        XMLStreamException {
        while (XmlParseUtil.nextChildElement(reader)) {
            Object element = this.readElement(reader, context);
            if (element instanceof BaseElement) {
                this.parseChild(model, (BaseElement) element);
            }
        }
    }

    protected void parseChild(M model, BaseElement child) throws ParseException{

    }



}
