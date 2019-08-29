package com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.InputDataItem;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.LoopCollection;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.MultiInstanceLoopCharacteristics;
import com.alibaba.smart.framework.engine.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  21:01.
 */
@ExtensionBinding(type = ExtensionConstant.ELEMENT_PARSER,binding = MultiInstanceLoopCharacteristics.class)

public class MultiInstanceLoopCharacteristicsParser extends AbstractElementParser<MultiInstanceLoopCharacteristics>
      {


    @Override
    public QName getQname() {
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
        multiInstanceLoopCharacteristics.setSequential(XmlParseUtil.getBoolean(reader, "isSequential", false));
        return multiInstanceLoopCharacteristics;
    }

    @Override
    protected void parseSingleChild(MultiInstanceLoopCharacteristics model, BaseElement child) throws ParseException {
        if (child instanceof LoopCollection) {
            model.setLoopCollection((LoopCollection)child);
        } else if (child instanceof InputDataItem) {
            model.setInputDataItemName(((InputDataItem)child).getName());
        }

        //else if (child instanceof CompletionCondition) {
        //    CompletionCondition completionCondition=(CompletionCondition)child;
        //    CompletionChecker completionChecker=model.getCompletionChecker();
        //
        //    if(null==completionChecker || !completionChecker.isCustom()){
        //        completionChecker = new CompletionChecker();
        //        completionChecker.setCustom(true);
        //    }
        //
        //    if(CompletionCondition.ACTION_ABORT.equals(completionCondition.getAction())){
        //        completionChecker.setAbortCheckPerformable(((CompletionCondition)child).getExpression());
        //    }else if(CompletionCondition.ACTION_CONTINUE.equals(completionCondition.getAction())){
        //        completionChecker.setCompletionCheckPerformable(((CompletionCondition)child).getExpression());
        //    }
        //    model.setCompletionChecker(completionChecker);
        //}else if (child instanceof CompletionCheckPrepare) {
        //    model.setCompletionCheckPrepare((CompletionCheckPrepare)child);
        //}


        else {
            throw  new EngineException("Should be a instance of CompletionCondition :"+child.getClass());
        }
    }
}