package com.alibaba.smart.framework.engine.model.assembly.impl;

import com.alibaba.smart.framework.engine.annoation.WorkAround;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionContainer;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
@Data
public abstract class AbstractProcessDefinition  implements ProcessDefinition {

    private static final long serialVersionUID = -1765647192018309663L;

    private String version;

    private String id;
    private String name;

    private ExtensionContainer extensionContainer;

    private com.alibaba.smart.framework.engine.model.assembly.Process process;

    @Override
    @WorkAround
    public String getVersion(){
        if(null == version){
            //compatible for empty version
                version = "1.0.0";
        }
        return this.version;
    }
}
