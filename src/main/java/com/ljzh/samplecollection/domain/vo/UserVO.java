package com.ljzh.samplecollection.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String password;
    private String code;
    private List<Long> roleIds;
}
