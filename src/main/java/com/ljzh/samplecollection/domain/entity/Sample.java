package com.ljzh.samplecollection.domain.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sample")
public class Sample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "the_geom", columnDefinition = "geometry")
    private Geometry theGeom;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_layer_id")
    private TaskLayer taskLayer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sample_tag_id")
    private SampleTag sampleTag;

    private Integer status;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

    // getter 和 setter


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

    public Geometry getTheGeom() {
        return theGeom;
    }

    public void setTheGeom(Geometry theGeom) {
        this.theGeom = theGeom;
    }

    public TaskLayer getTaskLayer() {
        return taskLayer;
    }

    public void setTaskLayer(TaskLayer taskLayer) {
        this.taskLayer = taskLayer;
    }

    public SampleTag getSampleTag() {
        return sampleTag;
    }

    public void setSampleTag(SampleTag sampleTag) {
        this.sampleTag = sampleTag;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
