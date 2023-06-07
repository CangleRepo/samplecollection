package com.ljzh.samplecollection.domain.dto;


import com.ljzh.samplecollection.constant.SampleStatus;

import javax.validation.constraints.NotNull;

public class SampleStatusUpdateDTO {
    @NotNull(message = "状态不能为空")
    private SampleStatus status;

    public SampleStatus getStatus() {
        return status;
    }

    public void setStatus(SampleStatus status) {
        this.status = status;
    }
}

