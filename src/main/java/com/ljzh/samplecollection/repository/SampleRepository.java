package com.ljzh.samplecollection.repository;

import com.ljzh.samplecollection.domain.entity.Sample;
import com.ljzh.samplecollection.domain.entity.TaskLayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SampleRepository extends JpaRepository<Sample, Long> {

    List<Sample> findByTaskLayer(TaskLayer taskLayer);
}
