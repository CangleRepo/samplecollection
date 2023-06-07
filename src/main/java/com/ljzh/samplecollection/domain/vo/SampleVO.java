package com.ljzh.samplecollection.domain.vo;

import com.ljzh.samplecollection.domain.entity.Sample;
import com.ljzh.samplecollection.domain.entity.SampleTag;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class SampleVO {
    private Long id;
    private String name;
    private String theGeom;
    private SampleTag sampleTag;
    private Integer sampleStatus;

    public static SampleVO from(Sample sample) {
        SampleVO vo = new SampleVO();
        vo.setId(sample.getId());
        vo.setName(sample.getName());
        vo.setTheGeom(sample.getTheGeom().toString());
        vo.setSampleTag(sample.getSampleTag());
        vo.setSampleStatus(sample.getStatus());
        return vo;
    }

    public Sample to() throws ParseException {
        Sample sample = new Sample();
        sample.setId(this.getId());
        sample.setName(this.getName());
        WKTReader wktReader = new WKTReader();
        sample.setTheGeom(wktReader.read(this.getTheGeom()));
        sample.setSampleTag(this.getSampleTag());
        sample.setStatus(this.getSampleStatus());
        return sample;
    }

    // getter 和 setter


    public Integer getSampleStatus() {
        return sampleStatus;
    }

    public void setSampleStatus(Integer sampleStatus) {
        this.sampleStatus = sampleStatus;
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

    public String getTheGeom() {
        return theGeom;
    }

    public void setTheGeom(String theGeom) {
        this.theGeom = theGeom;
    }

    public SampleTag getSampleTag() {
        return sampleTag;
    }

    public void setSampleTag(SampleTag sampleTag) {
        this.sampleTag = sampleTag;
    }
}
