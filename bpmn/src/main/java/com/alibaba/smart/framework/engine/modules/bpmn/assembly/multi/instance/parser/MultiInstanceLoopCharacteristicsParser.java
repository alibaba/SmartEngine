package com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.CompletionCheckPrepare;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.CompletionChecker;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.CompletionCondition;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.InputDataItem;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.LoopCollection;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.MultiInstanceLoopCharacteristics;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.impl.AbstractElementParser;

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
         {
        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = new MultiInstanceLoopCharacteristics();
        multiInstanceLoopCharacteristics.setSequential(this.getBoolean(reader, "isSequential", false));
        return multiInstanceLoopCharacteristics;
    }

    @Override
    protected void parseChild(MultiInstanceLoopCharacteristics model, BaseElement child) throws ParseException {
        if (child instanceof LoopCollection) {
            model.setLoopCollection((LoopCollection)child);
        } else if (child instanceof InputDataItem) {
            model.setInputDataItemName(((InputDataItem)child).getName());
        } else if (child instanceof CompletionCondition) {
            CompletionCondition completionCondition=(CompletionCondition)child;
            CompletionChecker completionChecker=model.getCompletionChecker();

            if(null==completionChecker || !completionChecker.isCustom()){
                completionChecker = new CompletionChecker();
                completionChecker.setCustom(true);
            }

            if(CompletionCondition.ACTION_ABORT.equals(completionCondition.getAction())){
                completionChecker.setAbortCheckPerformable(((CompletionCondition)child).getExpression());
            }else if(CompletionCondition.ACTION_CONTINUE.equals(completionCondition.getAction())){
                completionChecker.setCompletionCheckPerformable(((CompletionCondition)child).getExpression());
            }
            model.setCompletionChecker(completionChecker);
        }else if (child instanceof CompletionCheckPrepare) {
            model.setCompletionCheckPrepare((CompletionCheckPrepare)child);
        }else {
            throw  new EngineException("Should be a instance of CompletionCondition :"+child.getClass());
        }
    }
}