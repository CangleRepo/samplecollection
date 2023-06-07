package com.ljzh.samplecollection.service;

import com.ljzh.samplecollection.constant.TaskLayerStatus;
import com.ljzh.samplecollection.domain.entity.TaskLayer;
import com.ljzh.samplecollection.repository.TaskLayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskLayerService {

    @Autowired
    private TaskLayerRepository taskLayerRepository;

    public TaskLayer create(TaskLayer taskLayer) {
        return taskLayerRepository.save(taskLayer);
    }

    public boolean delete(Long id) {
        taskLayerRepository.deleteById(id);
        return true;
    }

    public TaskLayer getById(Long id) {
        return taskLayerRepository.findById(id).orElse(null);
    }

    public List<TaskLayer> getByTaskId(Long taskId) {
        return taskLayerRepository.findByTaskId(taskId);
    }

    public List<TaskLayer> getByLayerId(Long layerId) {
        return taskLayerRepository.findByLayerId(layerId);
    }

    public List<TaskLayer> getByStatus(TaskLayerStatus status) {
        return taskLayerRepository.findByStatus(status.code());
    }

    public List<TaskLayer> getAll() {
        return taskLayerRepository.findAll();
    }

    public void updateStatus(Long id, TaskLayerStatus status) {
        TaskLayer taskLayer = getById(id);
        if (taskLayer != null) {
            taskLayer.setStatus(status.code());
            taskLayerRepository.save(taskLayer);
        }
    }
}

