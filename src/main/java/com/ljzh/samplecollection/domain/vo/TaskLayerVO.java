package com.ljzh.samplecollection.domain.vo;

import com.ljzh.samplecollection.domain.entity.Layer;
import com.ljzh.samplecollection.domain.entity.Task;
import com.ljzh.samplecollection.domain.entity.TaskLayer;

import java.time.LocalDateTime;

public class TaskLayerVO {

    private Long id;
    private Long taskId;
    private Long layerId;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public static TaskLayerVO from(TaskLayer taskLayer) {
        TaskLayerVO vo = new TaskLayerVO();
        vo.setId(taskLayer.getId());
        vo.setTaskId(taskLayer.getTask().getId());
        vo.setLayerId(taskLayer.getLayer().getId());
        vo.setStatus(taskLayer.getStatus());
        vo.setCreateTime(taskLayer.getCreateTime());
        vo.setUpdateTime(taskLayer.getUpdateTime());
        return vo;
    }
    public TaskLayer to() {
        TaskLayer taskLayer = new TaskLayer();
        taskLayer.setId(this.getId());
        taskLayer.setTask(new Task(this.getTaskId()));
        taskLayer.setLayer(new Layer(this.getLayerId()));
        taskLayer.setStatus(this.getStatus());
        taskLayer.setCreateTime(this.getCreateTime());
        taskLayer.setUpdateTime(this.getUpdateTime());
        return taskLayer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getLayerId() {
        return layerId;
    }

    public void setLayerId(Long layerId) {
        this.layerId = layerId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}


