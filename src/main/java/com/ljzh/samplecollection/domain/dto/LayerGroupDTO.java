package com.ljzh.samplecollection.domain.dto;

import java.util.ArrayList;
import java.util.List;

public class LayerGroupDTO {
    List<Long> layerGroupIds = new ArrayList<>();

    public List<Long> getLayerGroupIds() {
        return layerGroupIds;
    }

    public void setLayerGroupIds(List<Long> layerGroupIds) {
        this.layerGroupIds = layerGroupIds;
    }

    public LayerGroupDTO() {
    }

    public LayerGroupDTO(List<Long> layerGroupIds) {
        this.layerGroupIds = layerGroupIds;
    }

    public LayerGroupDTO(Long groupId){
        this.layerGroupIds.add(groupId);
    }
}
