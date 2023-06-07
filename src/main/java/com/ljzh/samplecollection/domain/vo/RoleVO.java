package com.ljzh.samplecollection.domain.vo;

import java.time.LocalDateTime;

public class RoleVO {

    private Long id;
    private String name;
    private String code;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 无参构造函数
    public RoleVO(){}

    // 带参构造函数
    public RoleVO(Long id, String name, String code, LocalDateTime createTime,
                  LocalDateTime updateTime) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    // getter 和 setter 方法
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    // toString 方法
    @Override
    public String toString() {
        return "RoleVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}

