package com.alibaba.smart.framework.engine.xml.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.exception.ParseException;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.extension.scanner.ExtensionBindingResult;
import com.alibaba.smart.framework.engine.extension.scanner.SimpleAnnotationScanner;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.util.ClassLoaderUtil;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public abstract class AbstractElementParser<M extends BaseElement> implements ElementParser<M> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractElementParser.class);

    //private ExtensionPointRegistry extensionPointRegistry;
    //private XmlParserExtensionPoint xmlParserExtensionPoint;
    //
    //// GETTER & SETTER
    //
    //protected ExtensionPointRegistry getExtensionPointRegistry() {
    //    return extensionPointRegistry;
    //}
    //
    //protected XmlParserExtensionPoint getXmlParserExtensionPoint() {
    //    return xmlParserExtensionPoint;
    //}

    //public AbstractElementParser(ExtensionPointRegistry extensionPointRegistry) {
    //    this.extensionPointRegistry = extensionPointRegistry;
    //}

    @Override
    public void start() {
        //this.xmlParserExtensionPoint = this.extensionPointRegistry.getExtensionPoint(XmlParserExtensionPoint.class);
    }

    @Override
    public void stop() {

    }


    protected Object readElement(XMLStreamReader reader, ParseContext context) {
        XmlParserExtensionPoint xmlParserExtensionPoint = getXmlParserExtensionPoint1();
        return xmlParserExtensionPoint.parseElement(reader, context);
    }



    @Override
    public M parseElement(XMLStreamReader reader, ParseContext context) throws XMLStreamException {
        M model=this.parseModel(reader,context);

        context=context.evolve(model);

        this.parseMultiAttributes(model,reader,context);

        try {
            this.parseMultiChildren(model,reader,context);
        } catch (XMLStreamException e) {
            throw new ParseException(e);
        }

        return model;
    }


    protected   M parseModel(XMLStreamReader reader, ParseContext context) {
       throw  new EngineException("should be overridden");
    }



    private void parseMultiAttributes(M model, XMLStreamReader reader, ParseContext context) {
        int attributeCount=reader.getAttributeCount();
        if(attributeCount>0){
            for (int i = 0; i < attributeCount; i++) {
                QName attributeQName=reader.getAttributeName(i);
                Object value=this.parseSingleAttribute(attributeQName,reader, context);
                if(null!=value && value instanceof BaseElement){
                    this.singingMagic(model, (BaseElement) value);
                }else{
                    LOGGER.debug(attributeQName +" is ignored when parsing attribute from "+ model);
                }
            }
        }
    }

    private Object parseSingleAttribute(QName attributeName, XMLStreamReader reader, ParseContext context) {

        XmlParserExtensionPoint xmlParserExtensionPoint = getXmlParserExtensionPoint1();
        return xmlParserExtensionPoint.parseAttribute(attributeName,reader, context);
    }


    //TUNE CACHE
    private XmlParserExtensionPoint getXmlParserExtensionPoint1() {
        ExtensionBindingResult extensionBindingResult = SimpleAnnotationScanner.getMap().get(
            ExtensionConstant.EXTENSION_POINT);
        Class aClass = extensionBindingResult.getBindings().get(XmlParserExtensionPoint.class.getName());
        Object o = ClassLoaderUtil.createNewInstance(aClass);
        return (XmlParserExtensionPoint)o;
    }

    private void parseMultiChildren(M model, XMLStreamReader reader, ParseContext context) throws XMLStreamException {
        while (XmlParseUtil.nextChildElement(reader)) {
            Object element = this.readElement(reader, context);
            if (element instanceof BaseElement) {
                this.singingMagic(model, (BaseElement) element);
            }
        }
    }

    //TUNE rename
    protected void singingMagic(M model, BaseElement child) {

    }



}
