//package com.alibaba.smart.framework.engine.modules.smart.assembly.extension.parser;
//
//import javax.xml.namespace.QName;
//import javax.xml.stream.XMLStreamReader;
//
//import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
//import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
//import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
//import com.alibaba.smart.framework.engine.model.assembly.Extension;
//import com.alibaba.smart.framework.engine.modules.smart.assembly.extension.ExtensionElements;
//import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
//import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
//
///**
// * Extension Elements Parser Created by ettear on 16-4-14.
// */
//@ExtensionBinding(type = ExtensionConstant.ELEMENT_PARSER, bindingTo = ExtensionElements.class)
//
//public class ExtensionElementsParser extends AbstractElementParser<ExtensionElements> {
//
//
//    @Override
//    protected ExtensionElements parseModel(XMLStreamReader reader, ParseContext context) {
//        return new ExtensionElements();
//    }
//
//    @Override
//    protected void singingMagic(ExtensionElements extensionElements, BaseElement child) {
//        if (child instanceof Extension) {
//            extensionElements.addAndDecorateExtension((Extension)child);
//        }
//    }
//
//    @Override
//    public QName getQname() {
//        return ExtensionElements.type;
//    }
//
//    @Override
//    public Class<ExtensionElements> getModelType() {
//        return ExtensionElements.class;
//    }
//}
