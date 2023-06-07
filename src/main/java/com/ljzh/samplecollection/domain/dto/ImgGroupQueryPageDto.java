package com.ljzh.samplecollection.domain.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ImgGroupQueryPageDto {
    private Integer pageIndex = 1;
    private Integer pageSize = 15;
    private String keywords;
    private Date startDate;
    private Date endDate;
}
