package com.ljzh.samplecollection.domain.dto;

import lombok.Data;

@Data
public class TaskLayerQueryDTO {
    private Long userId;
    private Long roleId;
    private Long taskId;
    private Integer status;
    private int pageNum;
    private int pageSize;

    // getter 和 setter 方法省略
}

