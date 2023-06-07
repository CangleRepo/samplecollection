package com.ljzh.samplecollection.constant;

public enum TaskLayerStatus {
    UNCOLLECTED(0,"待采集"),
    COLLECTING(1,"已采集"),
    UNAUDITED(2,"待审核"),
    AUDITING(3,"已审核"),
    DONE(4,"已完成");

    private final String description;
    private final Integer code;

    TaskLayerStatus(Integer code,String description) {
        this.code = code;
        this.description = description;
    }

    public Integer code(){
        return code;
    }

    public String description() {
        return description;
    }
}
