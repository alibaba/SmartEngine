package com.alibaba.smart.framework.engine.xml.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.exception.ParseException;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */

public abstract class AbstractElementParser<M extends BaseElement> implements ElementParser<M> ,
    ProcessEngineConfigurationAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractElementParser.class);

    protected ProcessEngineConfiguration processEngineConfiguration;

    private XmlParserFacade xmlParserFacade;

    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }

    @Override
    public void start() {
        AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();
        this.xmlParserFacade =  annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,
            XmlParserFacade.class);
    }

    @Override
    public void stop() {

    }


    protected Object readElement(XMLStreamReader reader, ParseContext context) {
        return xmlParserFacade.parseElement(reader, context);
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


    protected    M parseModel(XMLStreamReader reader, ParseContext context) {
              throw  new EngineException("should be overridden");
    }

    private void parseMultiAttributes(M model, XMLStreamReader reader, ParseContext context) {
        int attributeCount=reader.getAttributeCount();
        if(attributeCount>0){
            for (int i = 0; i < attributeCount; i++) {
                QName attributeQName=reader.getAttributeName(i);
                Object value=this.parseSingleAttribute(attributeQName,reader, context);
                if(null!=value && value instanceof BaseElement){
                    this.decorateChild(model, (BaseElement) value, context);
                }else{
                    LOGGER.debug(attributeQName +" is ignored when parsing attribute from "+ model);
                }
            }
        }
    }

    private Object parseSingleAttribute(QName attributeName, XMLStreamReader reader, ParseContext context) {

        return xmlParserFacade.parseAttribute(attributeName,reader, context);
    }



    private void parseMultiChildren(M model, XMLStreamReader reader, ParseContext context) throws XMLStreamException {
        while (XmlParseUtil.nextChildElement(reader)) {
            Object element = this.readElement(reader, context);
            if (element instanceof BaseElement) {
                this.decorateChild(model, (BaseElement) element,context );
            }
        }
    }

    protected void decorateChild(M model, BaseElement child, ParseContext context) {

    }

}
