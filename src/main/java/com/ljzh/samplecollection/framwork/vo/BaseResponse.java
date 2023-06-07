package com.ljzh.samplecollection.framwork.vo;

import io.swagger.annotations.ApiModelProperty;

public class BaseResponse<T> {

    @ApiModelProperty(value = "返回代码")
    private Integer code;

    @ApiModelProperty(value = "描述")
    private String desc;

    @ApiModelProperty(value = "数据")
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BaseResponse() {
    }

    public BaseResponse(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public BaseResponse(Integer code, String desc, T data) {
        this.code = code;
        this.desc = desc;
        this.data = data;
    }
}