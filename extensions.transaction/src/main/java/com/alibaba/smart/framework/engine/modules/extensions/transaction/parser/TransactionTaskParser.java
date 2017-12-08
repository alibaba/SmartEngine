package com.alibaba.smart.framework.engine.modules.extensions.transaction.parser;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.node.ErrorStrategy;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.node.SingleTask;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.node.TransactionTask;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.transaction.*;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.util.SerializeUtils;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;
import com.taobao.metaq.client.MetaPushConsumer;
import jdk.nashorn.internal.runtime.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.List;

/**
 * @author Leo.yy   Created on 17/7/31.
 * @description
 * @see
 */
public class TransactionTaskParser extends AbstractBpmnActivityParser<TransactionTask> implements StAXArtifactParser<TransactionTask> {

    private final static Logger logger = LoggerFactory.getLogger(TransactionTaskParser.class);

    public TransactionTaskParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public TransactionTask parseModel(XMLStreamReader xmlStreamReader, ParseContext parseContext) throws ParseException, XMLStreamException {

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

        String mode = getString(xmlStreamReader, "mode");
        if (mode == null) {
            // default metaq
            mode = MODE_METAQ;
        }

        if (!mode.equals(MODE_LOCAL) && !mode.equals(MODE_METAQ)) {
            throw new ParserException("illegal mode name:" + mode);
        }
        task.setMode(mode);

        String metaGroup = getString(xmlStreamReader, "meta-group");
        if (metaGroup == null) {
            throw new ParserException("group can not be null");
        }
        task.setMetaGroup(metaGroup);

        String metaTopic = getString(xmlStreamReader, "meta-topic");
        if (metaTopic == null) {
            throw new ParserException("topic can not be null");
        }
        task.setMetaTopic(metaTopic);

        // register transaction processor
        if (!TransactionProcessorHolder.isRegisterd()) {
            TransactionProcessor processor;
            if (mode.equals(MODE_METAQ)) {
                processor = new MetaqAsyncTransactionProcessor(metaTopic, metaGroup);

                try {
                    initMetaConsumer(metaGroup, metaTopic, processor);
                } catch (MQClientException e) {
                    throw new RuntimeException("fail to init metaq client:" + e.getMessage());
                }

            } else {
                processor = new LocalAsyncTransactionProcessor();
            }

            TransactionProcessorHolder.register(processor);
        }

//        while (this.nextChildElement(xmlStreamReader)) {
//            Object element = this.readElement(xmlStreamReader, parseContext);
//            if (element instanceof BaseElement) {
//                this.parseChild(task, (BaseElement) element);
//                skipToEndElement(xmlStreamReader);
//            }
//        }


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


    private void initMetaConsumer(String metaGroup, String metaTopic, final TransactionProcessor transactionProcessor) throws MQClientException {
        MetaPushConsumer metaPushConsumer = new MetaPushConsumer(metaGroup);
        metaPushConsumer.subscribe(metaTopic, "*");
        metaPushConsumer.setConsumeMessageBatchMaxSize(1);
        metaPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

                for (MessageExt msg : msgs) {
                    logger.info("consume msg id:" + msg.getMsgId() + ", reconsume times:" + msg.getReconsumeTimes());
                    String tag = msg.getTags();
                    boolean result;
                    try {
                        TransactionProcessContext processContext = SerializeUtils.deSerialize(msg.getBody());
                        if (tag.equals("rollback")) {
                            result = transactionProcessor.rollback(processContext);
                        } else {
                            result = transactionProcessor.redo(processContext);
                        }
                    } catch (Exception e) {
                        result = false;
                        logger.error("consume Transaction Task Message error", e);

                    }

                    if (!result && msg.getReconsumeTimes() < MAX_RETRY_COUNT) {
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                }

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }

        });

        metaPushConsumer.start();
    }

    private static int MAX_RETRY_COUNT = 20;

    static {
        String maxRetry = System.getProperty("smart.engine.extensions.transaction.max.retry");
        if (maxRetry != null) {
            try {
                MAX_RETRY_COUNT = Integer.parseInt(maxRetry);
            } catch (Exception e) {
            }
        }
    }

    private final static String MODE_LOCAL = "local";
    private final static String MODE_METAQ = "metaq";
}
