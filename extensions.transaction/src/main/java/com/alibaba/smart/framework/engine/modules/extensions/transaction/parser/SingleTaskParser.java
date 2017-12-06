package com.alibaba.smart.framework.engine.modules.extensions.transaction.parser;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.node.ErrorStrategy;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.node.SingleTask;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * @author Leo.yy   Created on 2017/8/2.
 * @description
 * @see
 */
public class SingleTaskParser extends AbstractBpmnActivityParser<SingleTask> implements StAXArtifactParser<SingleTask> {

    public SingleTaskParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public SingleTask parseModel(XMLStreamReader xmlStreamReader, ParseContext parseContext) throws ParseException, XMLStreamException {

        SingleTask task = new SingleTask();

        String id = this.getString(xmlStreamReader, "id");
        task.setId(id);

        String beanName = this.getString(xmlStreamReader, "bean");
        task.setActionBeanName(beanName);

        String errorStrategy = this.getString(xmlStreamReader, "errorStrategy");
        task.setErrorStrategy(ErrorStrategy.getByName(errorStrategy));

//        Object action = SpringContextUtil.getBean(beanName);
//        if (action != null) {
//            if (!(action instanceof SingleTaskAction)) {
//                throw new RuntimeException(String.format("action bean specified for %s task is not type of SingleTaskAction ", id));
//            }
//
//            task.setAction((SingleTaskAction) action);
//        }


        return task;
    }


    @Override
    public QName getArtifactType() {
        return SingleTask.artifactType;
    }

    @Override
    public Class<SingleTask> getModelType() {
        return SingleTask.class;
    }
}
