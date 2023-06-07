package com.ljzh.samplecollection.domain.vo;

import com.ljzh.samplecollection.domain.entity.SampleTag;

import java.time.LocalDateTime;

public class SampleTagVO {
    private Long id;
    private String name;
    private String style;
    private String description;
    private byte[] icon;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public SampleTagVO() {}

    public SampleTagVO(SampleTag sampleTag) {
        this.id = sampleTag.getId();
        this.name = sampleTag.getName();
        if (sampleTag.getStyle() != null) {
            this.style = sampleTag.getStyle();
        }
        this.description = sampleTag.getDescription();
        this.icon = sampleTag.getIcon();
        this.createTime = sampleTag.getCreateTime();
        this.updateTime = sampleTag.getUpdateTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}

