package com.alibaba.smart.framework.engine.modules.compatible.activiti.assembly.multi.instance.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.MultiInstanceLoopCharacteristics;
import com.alibaba.smart.framework.engine.modules.compatible.activiti.assembly.multi.instance.ActivitiCollection;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.AttributeParser;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public class ActivitiCollectionParser extends AbstractElementParser<ActivitiCollection> implements
    AttributeParser<ActivitiCollection> {

    public ActivitiCollectionParser(
        ExtensionPointRegistry extensionPointRegistry) {
    }

    @Override
    public ActivitiCollection parseAttribute(QName attributeName, XMLStreamReader reader, ParseContext context) {
        ActivitiCollection activitiCollection = new ActivitiCollection();

        Object element=context.getCurrentElement();
        if(null!=element && element instanceof MultiInstanceLoopCharacteristics){
            MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics=(MultiInstanceLoopCharacteristics)element;

            //multiInstanceLoopCharacteristics.setCompletionCheckPrepare(new CompletionCheckPreparePerformable());
            //
            ////创建一个All模式的Checker，不是All模式会被覆盖
            //CompletionChecker completionChecker=new CompletionChecker();
            //completionChecker.setCustom(false);
            //completionChecker.setAbortCheckPerformable(new AbortCheckPerformable());
            //completionChecker.setCompletionCheckPerformable(new CompletionCheckPerformable());
            //multiInstanceLoopCharacteristics.setCompletionChecker(completionChecker);
        }
        return activitiCollection;
    }

    @Override
    public QName getQname() {
        return ActivitiCollection.type;
    }

    @Override
    public Class<ActivitiCollection> getModelType() {
        return ActivitiCollection.class;
    }
}
