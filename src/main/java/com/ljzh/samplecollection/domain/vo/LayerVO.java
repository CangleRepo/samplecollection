package com.ljzh.samplecollection.domain.vo;

import com.ljzh.samplecollection.domain.entity.Layer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LayerVO {
    private Long id;
    private String name;
    private String path;
    private int width;
    private int height;
    private Long layerGroupId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public LayerVO() {}

    public LayerVO(Layer layer) {
        this.id = layer.getId();
        this.name = layer.getName();
        this.path = layer.getPath();
        this.width = layer.getWidth();
        this.height = layer.getHeight();
        this.layerGroupId = layer.getGroupId();
        this.createTime = layer.getCreateTime();
        this.updateTime = layer.getUpdateTime();
    }

    // 省略 Getter 和 Setter 方法
}


