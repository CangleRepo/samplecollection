package com.ljzh.samplecollection.xmlbody;

import lombok.Data;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Object {
    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "pose")
    private int pose;

    @XmlElement(name = "truncated")
    private int truncated;

    @XmlElement(name = "difficult")
    private int difficult;

    @XmlElement(name = "bndbox")
    private BndBox bndBox;

    // 构造函数、getter 和 setter 方法省略
}

