package com.ljzh.samplecollection.domain.view;


import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@ToString
@Table(name = "task_layer_view")
public class TaskLayerView {
    @Id
    private Long taskLayerId;
    private Integer assignStatus;
    private Long taskId;

    public TaskLayerView() {
    }

    public TaskLayerView(Long taskLayerId, Integer assignStatus, Long taskId) {
        this.taskLayerId = taskLayerId;
        this.assignStatus = assignStatus;
        this.taskId = taskId;
    }

    public Long getTaskLayerId() {
        return taskLayerId;
    }

    public void setTaskLayerId(Long taskLayerId) {
        this.taskLayerId = taskLayerId;
    }

    public Integer getAssignStatus() {
        return assignStatus;
    }

    public void setAssignStatus(Integer assignStatus) {
        this.assignStatus = assignStatus;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    

}
