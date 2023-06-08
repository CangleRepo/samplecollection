package com.ljzh.samplecollection.domain.vo;

import java.util.List;

public class TaskAssignVO {
    private List<Long> userIds;

    private Long roleId;

    private List<Long> taskLayerIds;

    // getter 和 setter

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public List<Long> getTaskLayerIds() {
        return taskLayerIds;
    }

    public void setTaskLayerIds(List<Long> taskLayerIds) {
        this.taskLayerIds = taskLayerIds;
    }
}

