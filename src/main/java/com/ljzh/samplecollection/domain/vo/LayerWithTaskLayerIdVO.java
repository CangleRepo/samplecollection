package com.ljzh.samplecollection.domain.vo;

import com.ljzh.samplecollection.domain.entity.Layer;
import lombok.Data;

/***
 * @title LayerWithTaskLayerIdVO
 * @description 含有TaskLayerId和对应的Layer的VO类
 * @author rubby
 * @version 1.0.0
 * @create 2023/06/13 下午 03:16
 **/

@Data
public class LayerWithTaskLayerIdVO {
    private Layer layer;
    private Long taskLayerId;

    public LayerWithTaskLayerIdVO(Layer layer, Long taskLayerId) {
        this.layer = layer;
        this.taskLayerId = taskLayerId;
    }

    public LayerWithTaskLayerIdVO() {
    }
}
