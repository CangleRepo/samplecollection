package com.ljzh.samplecollection.repository;


import com.ljzh.samplecollection.domain.entity.TaskLayer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskLayerRepository extends JpaRepository<TaskLayer, Long> {
    List<TaskLayer> findByStatus(Integer code);

    List<TaskLayer> findByLayerId(Long layerId);

    List<TaskLayer> findByTaskId(Long taskId);

    TaskLayer findByLayerIdAndTaskId(Long id, Long taskId);
  
    Page<TaskLayer> findByIdIn(List<Long> taskLayerIds, Pageable pageable);
  
    List<TaskLayer> findByIdIn(List<Long> taskLayerIds);
}
