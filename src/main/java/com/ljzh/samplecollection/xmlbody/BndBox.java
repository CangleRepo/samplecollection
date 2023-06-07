package com.ljzh.samplecollection.xmlbody;

import lombok.Data;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class BndBox {
    @XmlElement(name = "xmin")
    private int xmin;

    @XmlElement(name = "ymin")
    private int ymin;

    @XmlElement(name = "xmax")
    private int xmax;

    @XmlElement(name = "ymax")
    private int ymax;

    // 构造函数、getter 和 setter 方法省略
}

