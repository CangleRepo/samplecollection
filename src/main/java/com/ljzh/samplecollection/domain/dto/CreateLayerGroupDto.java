package com.ljzh.samplecollection.domain.dto;

import lombok.Data;

@Data
public class CreateLayerGroupDto {
    private String name;
    private String description;
    private Long regionId;
}
