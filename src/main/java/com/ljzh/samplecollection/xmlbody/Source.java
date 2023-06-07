package com.ljzh.samplecollection.xmlbody;

import lombok.Data;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Source {
    @XmlElement(name = "database")
    private String database;

    public Source() {
        this.database = "Unknown";
    }

    // 构造函数、getter 和 setter 方法省略
}
