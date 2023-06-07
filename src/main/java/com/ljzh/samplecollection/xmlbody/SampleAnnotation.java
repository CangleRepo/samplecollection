package com.ljzh.samplecollection.xmlbody;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "annotation")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SampleAnnotation {
    private String folder;
    private String filename;
    private String path;

    @XmlElement(name = "source")
    private Source source;

    @XmlElement(name = "object")
    private List<Object> objectList;

    @XmlElement(name = "size")
    private Size size;

    @XmlElement(name = "segmented")
    private int segmented;

}

