package com.ljzh.samplecollection.constant;


public enum TaskAssignStatus {
    UNDISTRIBUTED(0, "未分配"), 
    ASSIGNED_COLLECTION(1, "已分配采集人员"),
    ASSIGNED_AUDIT(2, "已分配审核人员");
    private final Integer code;
    private final String description;

    TaskAssignStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer code() {
        return code;
    }

    public String description() {
        return description;
    }
}
