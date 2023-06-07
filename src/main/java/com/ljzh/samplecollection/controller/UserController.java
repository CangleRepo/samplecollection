package com.ljzh.samplecollection.controller;

import com.ljzh.samplecollection.domain.entity.User;
import com.ljzh.samplecollection.domain.entity.UserRole;
import com.ljzh.samplecollection.domain.dto.LoginRequest;
import com.ljzh.samplecollection.domain.vo.UserVO;
import com.ljzh.samplecollection.framwork.exception.CustomException;
import com.ljzh.samplecollection.framwork.utils.ResponseUtils;
import com.ljzh.samplecollection.framwork.vo.BaseResponse;
import com.ljzh.samplecollection.service.UserRoleService;
import com.ljzh.samplecollection.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Api(tags = "用户相关接口")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;

    @PostMapping("/login")
    @ApiOperation("登录")
    public BaseResponse<String> login(@RequestBody LoginRequest request) {
        if (request.getUserName() == null || request.getUserName().isEmpty()) {
            throw new CustomException("Username cannot be null or empty");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new CustomException("Password cannot be null or empty");
        }
        User user = userService.findUserByUsername(request.getUserName());
        if (!request.getPassword().equals(user.getPassword())) {
            throw new CustomException("Incorrect password");
        }
        List<UserRole> userRoles = userRoleService.findUserRoleByUserId(user.getId());
        boolean roleIdExists = userRoles.stream()
                .anyMatch(userRole -> userRole.getRoleId().equals(request.getRoleId()));
        if (!roleIdExists) {
            throw new IllegalArgumentException("Invalid role");
        }
        return ResponseUtils.getSuccessResponse(user.getId().toString());
    }

    // 新增用户
    @PostMapping
    @ApiOperation("新增用户")
    public BaseResponse<User> saveUserWithRoles(@RequestBody UserVO userVO) {
        return ResponseUtils.getSuccessResponse(userService.saveUserWithRoles(userVO));
    }

    // 根据id删除用户
    @DeleteMapping("/{id}")
    @ApiOperation("根据id删除用户")
    public BaseResponse<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseUtils.getSuccessResponse();
    }

    // 更新用户信息
    @PutMapping("/")
    @ApiOperation("更新用户信息")
    public BaseResponse<Void> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseUtils.getSuccessResponse();
    }

    // 根据id查询用户
    @GetMapping("/{id}")
    @ApiOperation("根据id查询用户")
    public BaseResponse<User> getUserById(@PathVariable Long id) {
        return ResponseUtils.getSuccessResponse(userService.getUserById(id));
    }

    // 查询所有用户
    @GetMapping("/")
    @ApiOperation("查询所有用户")
    public BaseResponse<List<User>> getAllUsers() {
        return ResponseUtils.getSuccessResponse(userService.getAllUsers());
    }

    // 根据用户名查询用户
    @GetMapping("/username/{username}")
    @ApiOperation("根据用户名查询用户")
    public BaseResponse<User> findUserByUsername(@PathVariable String username) {
        return ResponseUtils.getSuccessResponse(userService.findUserByUsername(username));
    }

    // 分页查询所有用户
    @GetMapping("/page")
    @ApiOperation("分页查询所有用户")
    public BaseResponse<Page<User>> getAllUsersByPage(@RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseUtils.getSuccessResponse(userService.getAllUsersByPage(pageNum, pageSize));
    }
}
