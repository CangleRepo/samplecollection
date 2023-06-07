package com.ljzh.samplecollection.repository;

import com.ljzh.samplecollection.domain.entity.LayerGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface LayerGroupRepository extends JpaRepository<LayerGroup, Long> {
    @Query("SELECT lg FROM LayerGroup lg WHERE lg.name LIKE :keywords OR lg.description LIKE :keywords")
    Page<LayerGroup> findAllByKeywords(String keywords, Pageable pageable);

    @Query("SELECT lg FROM LayerGroup lg WHERE (lg.name LIKE :keywords OR lg.description LIKE :keywords) AND lg.updateTime BETWEEN :startTime AND :endTime")
    Page<LayerGroup> findAllByKeywordsAndUpdateTimeBetween(String keywords, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    Page<LayerGroup> findAllByUpdateTimeBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
}
