package com.alibaba.smart.framework.engine.ecology.designer.bpmn;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Map;

/**
 * BPMN用户任务
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserTask extends AbstractBpmnElement {
    private String smartClass;
    private String assignee;
    private String candidateUsers;
    private String candidateGroups;
    private Integer priority;
    
    @Override
    public String getElementType() {
        return "userTask";
    }
    
    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> attrs = super.getAttributes();
        if (smartClass != null) {
            attrs.put("smart:class", smartClass);
        }
        if (assignee != null) {
            attrs.put("assignee", assignee);
        }
        if (candidateUsers != null) {
            attrs.put("candidateUsers", candidateUsers);
        }
        if (candidateGroups != null) {
            attrs.put("candidateGroups", candidateGroups);
        }
        if (priority != null) {
            attrs.put("priority", priority.toString());
        }
        return attrs;
    }
}
