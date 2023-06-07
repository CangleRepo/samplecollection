package com.ljzh.samplecollection.domain.dto;

import com.ljzh.samplecollection.constant.TaskLayerStatus;

import javax.validation.constraints.NotNull;

public class TaskLayerStatusUpdateDTO {
    @NotNull(message = "状态不能为空")
    private TaskLayerStatus status;

    public TaskLayerStatus getStatus() {
        return status;
    }

    public void setStatus(TaskLayerStatus status) {
        this.status = status;
    }
}


