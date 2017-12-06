package com.alibaba.smart.framework.engine.modules.extensions.transaction.parser;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.node.ErrorStrategy;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.node.SingleTask;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.node.TransactionTask;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * @author Leo.yy   Created on 17/7/31.
 * @description
 * @see
 */
public class TransactionTaskParser extends AbstractBpmnActivityParser<TransactionTask> implements StAXArtifactParser<TransactionTask> {

    public TransactionTaskParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public TransactionTask parse(XMLStreamReader xmlStreamReader, ParseContext parseContext) throws ParseException, XMLStreamException {

        TransactionTask task = new TransactionTask();

        String id = getString(xmlStreamReader, "id");
        task.setId(id);

        String errorStrategy = getString(xmlStreamReader, "errorStrategy");
        ErrorStrategy strategy = ErrorStrategy.getByName(errorStrategy);
        task.setErrorStrategy(strategy);

        String maxRedoCount = getString(xmlStreamReader, "maxRedoCount");
        if (maxRedoCount != null) {
            try {
                task.setMaxRedoCount(Integer.parseInt(maxRedoCount));
            } catch (Exception e) {
            }
        }

        while (this.nextChildElement(xmlStreamReader)) {
            Object element = this.readElement(xmlStreamReader, parseContext);
            if (element instanceof BaseElement) {
                this.parseChild(task, (BaseElement) element);
                skipToEndElement(xmlStreamReader);
            }
        }


        return task;
    }


    @Override
    protected void parseChild(TransactionTask model, BaseElement child) {
        if (child instanceof SingleTask) {
            model.getChildTasks().add((SingleTask) child);
        }

    }

    @Override
    public QName getArtifactType() {
        return TransactionTask.artifactType;
    }

    @Override
    public Class<TransactionTask> getModelType() {
        return TransactionTask.class;
    }
}
