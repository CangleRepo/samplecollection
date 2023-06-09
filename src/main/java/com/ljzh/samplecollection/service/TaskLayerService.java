package com.ljzh.samplecollection.service;

import com.ljzh.samplecollection.constant.DefaultPage;
import com.ljzh.samplecollection.constant.TaskAssignStatus;
import com.ljzh.samplecollection.constant.TaskLayerStatus;
import com.ljzh.samplecollection.domain.entity.Layer;
import com.ljzh.samplecollection.domain.entity.TaskLayer;
import com.ljzh.samplecollection.domain.view.TaskLayerView;
import com.ljzh.samplecollection.framwork.constant.ResponseEnum;
import com.ljzh.samplecollection.framwork.exception.CustomException;
import com.ljzh.samplecollection.repository.TaskLayerRepository;
import com.ljzh.samplecollection.repository.TaskLayerViewRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class TaskLayerService {

    @Autowired
    private TaskLayerRepository taskLayerRepository;

    @Autowired
    private TaskLayerViewRepository taskLayerViewRepository;


    public TaskLayer create(TaskLayer taskLayer) {
        return taskLayerRepository.save(taskLayer);
    }

    public Page<Layer> findLayersByTaskIdAndAssignStatusPage(int pageNum, int pageSize, Integer assignStatus, Long taskId) {
        if (!(TaskAssignStatus.ASSIGNED_AUDIT.code().equals(assignStatus) || TaskAssignStatus.ASSIGNED_COLLECTION.code().equals(assignStatus) || TaskAssignStatus.UNDISTRIBUTED.code().equals(assignStatus))) {
            throw new CustomException(ResponseEnum.ILLEGAL_STATE);
        }
        if (pageNum < 1) {
            pageNum = DefaultPage.DEFAULT_PAGE_NUM.getValue();
        }
        if (pageSize < 1) {
            pageSize = DefaultPage.DEFAULT_PAGE_SIZE.getValue();
        }
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        List<Long> taskLayerIds = taskLayerViewRepository.findByAssignStatusAndTaskId(assignStatus, taskId).stream().map(TaskLayerView::getTaskLayerId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(taskLayerIds)) {
            throw new CustomException(ResponseEnum.TASK_NOT_EXITS);
        }
        List<Layer> layers = taskLayerRepository.findByIdIn(taskLayerIds).stream().map(TaskLayer::getLayer).distinct().collect(Collectors.toList());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), layers.size());
        if (start >= layers.size()) {
            throw new CustomException(ResponseEnum.ARRAY_OUT_OF_BOUND);
        }
        return new PageImpl<>(layers.subList(start, end), pageable, layers.size());
    }


    public boolean delete(Long id) {
        if (id == null || id <= 0) {
            throw new CustomException(ResponseEnum.PARAM_ERROR);
        }
        try {
            TaskLayerView taskLayerView = taskLayerViewRepository.findByTaskLayerId(id);
            if (taskLayerView == null) {
                throw new CustomException(ResponseEnum.TASK_NOT_EXITS);
            }
            if (TaskAssignStatus.ASSIGNED_AUDIT.code().equals(taskLayerView.getAssignStatus()) || TaskAssignStatus.ASSIGNED_COLLECTION.code().equals(taskLayerView.getAssignStatus())) {
                log.info("info :{} ", ResponseEnum.TASK_ASSIGNED.getDesc());
                return false;
            } else {
                taskLayerRepository.deleteById(id);
                return true;
            }
        } catch (JpaSystemException e) {
            throw new CustomException(ResponseEnum.SERVICE_INNER_ERROR);
        }
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

