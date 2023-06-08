package com.ljzh.samplecollection.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserWithLayersCountVO extends UserVO{
    private Integer currentLayersNum;
    private Integer allLayersNum;
    public String toString(){
        return username+"  "+currentLayersNum+"  "+allLayersNum;
    }
}
