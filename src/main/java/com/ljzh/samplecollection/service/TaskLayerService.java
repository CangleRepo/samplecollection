package com.ljzh.samplecollection.service;

import com.ljzh.samplecollection.constant.TaskAssignStatus;
import com.ljzh.samplecollection.constant.TaskLayerStatus;
import com.ljzh.samplecollection.domain.entity.Layer;
import com.ljzh.samplecollection.domain.entity.TaskLayer;
import com.ljzh.samplecollection.domain.view.TaskLayerView;
import com.ljzh.samplecollection.framwork.constant.ResponseEnum;
import com.ljzh.samplecollection.framwork.exception.CustomException;
import com.ljzh.samplecollection.repository.LayerRepository;
import com.ljzh.samplecollection.repository.TaskLayerRepository;
import com.ljzh.samplecollection.repository.TaskLayerViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskLayerService {

    @Autowired
    private TaskLayerRepository taskLayerRepository;

    @Autowired
    private TaskLayerViewRepository taskLayerViewRepository;




    public TaskLayer create(TaskLayer taskLayer) {
        return taskLayerRepository.save(taskLayer);
    }

    public Page<Layer> findLayersByTaskIdAndAssignStatusPage(int pageNum, int pageSize, Integer assignStatus, Long taskId) {
        if (!(assignStatus.equals(TaskAssignStatus.ASSIGNED_AUDIT.code()) ||
                assignStatus.equals(TaskAssignStatus.ASSIGNED_COLLECTION.code()) ||
                assignStatus.equals(TaskAssignStatus.UNDISTRIBUTED.code()))) {
            throw new CustomException(ResponseEnum.ILLEGAL_STATE);
        }

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        List<Long> taskLayerIds = taskLayerViewRepository.findByAssignStatusAndTaskId(assignStatus, taskId).stream().map(TaskLayerView::getTaskLayerId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(taskLayerIds)) {
            throw new CustomException("The task layer does not exist");
        }
        System.out.println("pageable.getOffset() = " + pageable.getOffset());
        List<Layer> layers = taskLayerRepository.findByIdIn(taskLayerIds).stream().map(TaskLayer::getLayer).distinct().collect(Collectors.toList());
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize() - 1) > layers.size() ? layers.size() : (start + pageable.getPageSize());
        if (start > layers.size()) {
            throw new CustomException("The page number is too large");
        }
        List<Layer> indexObjects = layers.subList(start, end);
        return new PageImpl<>(indexObjects, pageable, layers.size());
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

