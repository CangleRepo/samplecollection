package com.ljzh.samplecollection.controller;

import cn.hutool.core.util.ObjectUtil;
import com.ljzh.samplecollection.domain.entity.Role;
import com.ljzh.samplecollection.domain.entity.UserRole;
import com.ljzh.samplecollection.domain.vo.RoleVO;
import com.ljzh.samplecollection.domain.vo.UserRoleVO;
import com.ljzh.samplecollection.framwork.utils.ResponseUtils;
import com.ljzh.samplecollection.framwork.vo.BaseResponse;
import com.ljzh.samplecollection.service.RoleService;
import com.ljzh.samplecollection.service.UserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@Api(tags = "角色相关接口")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @PostMapping
    @ApiOperation("新增角色")
    public BaseResponse<Role> saveRole(@RequestBody RoleVO roleVO) {
        if (roleService.isRoleNameExists(roleVO.getName())) {
            return ResponseUtils.getErrorResponse();
        }
        Role savedRole = roleService.saveRole(roleVO);
        return ResponseUtils.getSuccessResponse(savedRole);
    }

    @PostMapping("/editUserRole")
    @ApiOperation("给人员添加角色或者修改角色")
    public BaseResponse<Boolean> editUserRole(@RequestBody UserRoleVO userRole) {
        if (ObjectUtil.isEmpty(userRole.getUserRoleId())){
            userRoleService.addUserRole(userRole.toUserRole());
        }
        else {
            userRoleService.updateUserRole(userRole.toUserRole());
        }
        return ResponseUtils.getSuccessResponse(true);
    }

    @PostMapping("/deleteRole/{roleId}")
    @ApiOperation("删除角色")
    public BaseResponse<Boolean> deleteRole(@PathVariable(value = "roleId") String roleId) {
        roleService.deleteRoleById(Long.valueOf(roleId));
        return ResponseUtils.getSuccessResponse(true);
    }

    @GetMapping()
    @ApiOperation("查询角色列表")
    public BaseResponse<List<RoleVO>> listRoles() {
        return ResponseUtils.getSuccessResponse(roleService.list());
    }
}

