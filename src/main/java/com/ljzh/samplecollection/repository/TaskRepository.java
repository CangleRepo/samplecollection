package com.ljzh.samplecollection.repository;

import com.ljzh.samplecollection.domain.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByIdIn(List<Long> taskIds);

    @Query("select distinct t from Task t where t.id in ?1")
    List<Task> findByIdInAndDistinct(List<Long> taskIds);
}
