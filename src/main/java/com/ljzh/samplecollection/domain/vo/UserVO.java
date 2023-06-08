package com.ljzh.samplecollection.domain.vo;

import com.ljzh.samplecollection.domain.entity.User;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
public class UserVO {
    protected Long id;
    protected String username;
    protected String password;
    protected String code;
    protected List<Long> roleIds;

    public UserVO(){

    }
    public UserVO(User user){
        id = user.getId();
        username = user.getUsername();
        password = user.getPassword();
        code = user.getCode();
    }
}
