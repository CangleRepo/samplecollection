package com.ljzh.samplecollection.repository;

import com.ljzh.samplecollection.domain.entity.Layer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LayerRepository extends JpaRepository<Layer, Long> {
    List<Layer> findByGroupId(Long layerGroupId);

    List<Layer> findAllByGroupIdIn(List<Long> layerGroupIds);
}
