package com.ljzh.samplecollection.controller;

import com.ljzh.samplecollection.domain.entity.Role;
import com.ljzh.samplecollection.domain.vo.RoleVO;
import com.ljzh.samplecollection.framwork.utils.ResponseUtils;
import com.ljzh.samplecollection.framwork.vo.BaseResponse;
import com.ljzh.samplecollection.service.RoleService;
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

    @PostMapping
    @ApiOperation("新增角色")
    public BaseResponse<Role> saveRole(@RequestBody RoleVO roleVO) {
        if (roleService.isRoleNameExists(roleVO.getName())) {
            return ResponseUtils.getErrorResponse();
        }
        Role savedRole = roleService.saveRole(roleVO);
        return ResponseUtils.getSuccessResponse(savedRole);
    }

    @GetMapping()
    @ApiOperation("查询角色列表")
    public BaseResponse<List<RoleVO>> listRoles() {
        return ResponseUtils.getSuccessResponse(roleService.list());
    }
}

