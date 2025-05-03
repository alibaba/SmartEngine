package com.alibaba.smart.framework.engine.bpmn.behavior.gateway.tree;

import java.util.ArrayList;
import java.util.List;


public class ActivityTreeNode {
    private String activityId;
    private List<ActivityTreeNode> children;
    private ActivityTreeNode parent;

    public ActivityTreeNode(String activityId) {
        this.activityId = activityId;
        this.children = new ArrayList<>();
        this.parent = null;
    }

    public String getActivityId() {
        return activityId;
    }

    public List<ActivityTreeNode> getChildren() {
        return children;
    }

    public ActivityTreeNode getParent() {
        return parent;
    }

    public void setParent(ActivityTreeNode parent) {
        this.parent = parent;
    }

    public void addChild(ActivityTreeNode child) {
        children.add(child);
        child.setParent(this);
    }
}