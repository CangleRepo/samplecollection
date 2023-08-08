package com.ljzh.samplecollection.domain.vo;

import com.ljzh.samplecollection.domain.entity.UserRole;

import java.time.LocalDate;
import java.util.Date;

public class UserRoleVO {
    private String userRoleId;
    private String userId;
    private String roleId;

    public String getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(String userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public UserRole toUserRole(){
        UserRole userRole = new UserRole();
        userRole.setId(Long.valueOf(this.userRoleId));
        userRole.setRoleId(Long.valueOf(this.roleId));
        userRole.setUserId(Long.valueOf(this.userId));
        return userRole;
    }
}
