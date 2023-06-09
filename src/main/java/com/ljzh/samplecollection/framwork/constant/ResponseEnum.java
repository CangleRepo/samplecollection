package com.ljzh.samplecollection.framwork.constant;

public enum ResponseEnum {

    SUCCESS(200, "成功"),
    FAILURE(201, "失败"),
    PARAM_ERROR(300, "参数错误"),
    PARAMS_CHECK_ERR(303, "参数校验错误"),
    JSON_DATABIND(400, "数据校验出错"),
    SERVICE_INNER_ERROR(500, "服务内部错误"),


    FILE_EXIST(10000, "文件已存在，请修改上传文件名"),
    TASK_NOT_EXITS(10001, "任务不存在"),
    LAYER_NOT_EXITS(10002, "图片不存在"),
    TASK_LAYER_LACK(10003, "任务图片缺失"),
    ILLEGAL_STATE(10004, "非法的角色"),
    USER_ROLE_NOT_EXIST(10005, "用户角色不存在"),
    TASK_LAYER_LACK_COLLECTOR(10006, "任务图片缺少采集员"),
    ARRAY_OUT_OF_BOUND(10007, "数组越界"),
    TASK_ASSIGNED(10008, "任务已分配"),
    PAGE_NUMBER_OUT_OF_RANGE(10009, "页码超出范围"),
    TASK_ASSIGN_EXIST(10010, "任务已分配");


    public final int code;
    public final String desc;

    ResponseEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
