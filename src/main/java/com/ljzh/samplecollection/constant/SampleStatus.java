package com.ljzh.samplecollection.constant;

public enum SampleStatus {
    UNAUDITED(0,"未审核"),
    ACCEPT(1,"通过"),
    REJECT(-1,"不通过"),
    DELETED(-2,"删除");

    private final String description;
    private final Integer code;

    SampleStatus(Integer code,String description) {
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

