package com.ljzh.samplecollection.repository;

import com.ljzh.samplecollection.domain.entity.SampleTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleTagRepository extends JpaRepository<SampleTag, Long> {
    SampleTag findByName(String text);
}
