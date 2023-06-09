package com.ljzh.samplecollection.repository;

import com.ljzh.samplecollection.domain.view.TaskLayerView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/***
 * @title TaskLayerViewRepository
 * @description 任务图层视图
 * @author rubby
 * @version 1.0.0
 * @create 2023/06/08 上午 09:42
 **/
public interface TaskLayerViewRepository extends JpaRepository<TaskLayerView, Long> {
    List<TaskLayerView> findByAssignStatusAndTaskId(Integer code, Long taskId);

    TaskLayerView findByTaskLayerId(Long taskLayerId);

}
