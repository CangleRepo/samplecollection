package com.ljzh.samplecollection.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ljzh.samplecollection.domain.entity.Layer;
import com.ljzh.samplecollection.domain.entity.LayerGroup;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class LayerGroupVO {
    private Long id;
    private Long regionId;
    private String name;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @JsonProperty(required = false)
    private List<LayerVO> layers;

    public LayerGroupVO() {}

    public LayerGroupVO(LayerGroup layerGroup) {
        this.id = layerGroup.getId();
        this.regionId = layerGroup.getRegionId();
        this.name = layerGroup.getName();
        this.description = layerGroup.getDescription();
        this.createTime = layerGroup.getCreateTime();
        this.updateTime = layerGroup.getUpdateTime();
        if (layerGroup.getLayers() != null) {
            List<LayerVO> layerVOList = new ArrayList<>();
            for (Layer layer : layerGroup.getLayers()) {
                layerVOList.add(new LayerVO(layer));
            }
            this.layers = layerVOList;
        }
    }
}

