package com.ljzh.samplecollection.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class LayerGroupPageVO {
    private List<LayerGroupVO> layerGroups;
    private int totalPages;
    private long totalElements;
    private int pageNumber;
    private int pageSize;

    // Constructors, getters, and setters
}
