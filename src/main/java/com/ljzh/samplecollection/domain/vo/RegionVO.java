package com.ljzh.samplecollection.domain.vo;

import com.ljzh.samplecollection.domain.entity.Region;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class RegionVO {
    private Long id;
    private String name;
    private Long parentId;
    private String code;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<RegionVO> children;

    public RegionVO() {}

    public RegionVO(Region region) {
        this.id = region.getId();
        this.name = region.getName();
        this.parentId = region.getParentId();
        this.code = region.getCode();
        this.createTime = region.getCreateTime();
        this.updateTime = region.getUpdateTime();
        List<RegionVO> children = new ArrayList<>();
        for (Region child : region.getChildren()) {
            children.add(new RegionVO(child));
        }
        this.children = children;
    }

    // 省略 Getter 和 Setter 方法
}
