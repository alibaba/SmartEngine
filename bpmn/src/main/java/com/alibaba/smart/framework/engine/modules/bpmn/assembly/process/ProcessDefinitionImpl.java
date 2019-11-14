package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process;

import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.annoation.WorkAround;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionContainer;
import com.alibaba.smart.framework.engine.model.assembly.IdBasedElement;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;

import lombok.Data;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
@Data
public class ProcessDefinitionImpl implements ProcessDefinition {


    private static final long serialVersionUID = -7973338663278156625L;

    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "process");

    private String id;

    private String version;

    private String name;

    private ExtensionContainer extensionContainer;

    private List<BaseElement> baseElementList;

    private Map<String, IdBasedElement> idBasedElementMap;


    @Override
    @WorkAround
    public String getVersion(){
        if(null == version){
            //compatible for empty version
            version = "1.0.0";
        }
        return this.version;
    }

    @Override
    public String toString() {
        return getId()+":"+getVersion();
    }

}
