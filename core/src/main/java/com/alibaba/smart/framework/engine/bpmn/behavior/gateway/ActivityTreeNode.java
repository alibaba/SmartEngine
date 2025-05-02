package com.alibaba.smart.framework.engine.bpmn.behavior.gateway;

import java.util.ArrayList;
import java.util.List;

public class ActivityTreeNode {
    private String activityId;
    private List<ActivityTreeNode> children;

    public ActivityTreeNode(String activityId) {
        this.activityId = activityId;
        this.children = new ArrayList<>();
    }

    public String getActivityId() {
        return activityId;
    }

    public List<ActivityTreeNode> getChildren() {
        return children;
    }

    public void addChild(ActivityTreeNode child) {
        children.add(child);
    }
}