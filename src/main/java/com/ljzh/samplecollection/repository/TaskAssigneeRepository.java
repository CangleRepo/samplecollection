package com.ljzh.samplecollection.repository;

import com.ljzh.samplecollection.domain.entity.TaskAssignee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskAssigneeRepository extends JpaRepository<TaskAssignee, Long> {
    List<TaskAssignee> findByUserIdAndRoleId(Long userId, Long roleId);

    List<TaskAssignee> findByUserIdAndRoleIdAndTaskId(Long userId, Long roleId, Long taskId);
}
