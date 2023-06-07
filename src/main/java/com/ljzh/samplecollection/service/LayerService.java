package com.ljzh.samplecollection.service;

import com.ljzh.samplecollection.domain.entity.Layer;
import com.ljzh.samplecollection.domain.entity.LayerGroup;
import com.ljzh.samplecollection.domain.vo.LayerVO;
import com.ljzh.samplecollection.repository.LayerGroupRepository;
import com.ljzh.samplecollection.repository.LayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LayerService {
    @Autowired
    private LayerRepository layerRepository;
    @Autowired
    private LayerGroupRepository layerGroupRepository;

    public LayerVO getById(Long id) {
        Layer layer = layerRepository.findById(id).orElse(null);
        if (layer == null) {
            return null;
        }
        return convertToVO(layer);
    }

    public List<LayerVO> getByLayerGroup(Long layerGroupId) {
        List<Layer> layers = layerRepository.findByGroupId(layerGroupId);
        if (layers.isEmpty()) {
            return Collections.emptyList();
        }
        return layers.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    public List<LayerVO> getAll() {
        List<Layer> layers = layerRepository.findAll();
        if (layers.isEmpty()) {
            return Collections.emptyList();
        }
        return layers.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    public Long create(LayerVO layerVO) {
        LayerGroup layerGroup = layerGroupRepository.findById(layerVO.getLayerGroupId()).orElse(null);
        if (layerGroup == null) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        Layer layer = new Layer();
        layer.setName(layerVO.getName());
        layer.setPath(layerVO.getPath());
        layer.setWidth(layerVO.getWidth());
        layer.setHeight(layerVO.getHeight());
        layer.setGroupId(layerGroup.getId());
        layer.setCreateTime(now);
        layer.setUpdateTime(now);
        return layerRepository.save(layer).getId();
    }

    public boolean delete(Long id) {
        Optional<Layer> optionalLayer = layerRepository.findById(id);
        if (optionalLayer.isPresent()) {
            layerRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean update(Long id, LayerVO layerVO) {
        Optional<Layer> optionalLayer = layerRepository.findById(id);
        if (!optionalLayer.isPresent()) {
            return false;
        }
        Layer layer = optionalLayer.get();
        if (layerVO.getLayerGroupId() != null) {
            LayerGroup layerGroup = layerGroupRepository.findById(layerVO.getLayerGroupId()).orElse(null);
            if (layerGroup == null) {
                return false;
            }
            layer.setGroupId(layerGroup.getId());
        }
        layer.setName(layerVO.getName());
        layer.setPath(layerVO.getPath());
        layer.setWidth(layerVO.getWidth());
        layer.setHeight(layerVO.getHeight());
        layer.setUpdateTime(LocalDateTime.now());
        layerRepository.save(layer);
        return true;
    }

    private LayerVO convertToVO(Layer layer) {
        LayerVO layerVO = new LayerVO();
        layerVO.setId(layer.getId());
        layerVO.setName(layer.getName());
        layerVO.setPath(layer.getPath());
        layerVO.setWidth(layer.getWidth());
        layerVO.setHeight(layer.getHeight());
        layerVO.setLayerGroupId(layer.getGroupId());
        layerVO.setCreateTime(layer.getCreateTime());
        layerVO.setUpdateTime(layer.getUpdateTime());
        return layerVO;
    }
}



