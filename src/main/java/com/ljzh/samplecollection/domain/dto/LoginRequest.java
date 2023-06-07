package com.ljzh.samplecollection.domain.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private Long roleId;
    private String userName;
    private String password;
}
