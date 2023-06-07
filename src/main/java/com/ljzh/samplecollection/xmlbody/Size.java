package com.ljzh.samplecollection.xmlbody;

import lombok.Data;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Size {
    @XmlElement(name = "width")
    private int width;

    @XmlElement(name = "height")
    private int height;

    @XmlElement(name = "depth")
    private int depth;

    // 构造函数、getter 和 setter 方法省略
}

