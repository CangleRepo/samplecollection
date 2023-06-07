package com.ljzh.samplecollection.service;

import com.ljzh.samplecollection.domain.entity.UserRole;
import com.ljzh.samplecollection.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService {
    @Autowired
    private UserRoleRepository userRoleRepository;

    // 新增用户角色
    public void addUserRole(UserRole userRole) {
        userRoleRepository.save(userRole);
    }

    // 根据id删除用户角色
    public void deleteUserRoleById(Long id) {
        userRoleRepository.deleteById(id);
    }

    // 更新用户角色信息
    public void updateUserRole(UserRole userRole) {
        userRoleRepository.save(userRole);
    }

    // 根据id查询用户角色
    public UserRole getUserRoleById(Long id) {
        return userRoleRepository.findById(id).orElse(null);
    }

    // 查询所有用户角色
    public List<UserRole> getAllUserRoles() {
        return userRoleRepository.findAll();
    }

    public Page<UserRole> getAllUserRolesByPage(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        return userRoleRepository.findAll(pageable);
    }

    public List<UserRole> findUserRoleByUserId(Long id) {

        return userRoleRepository.findUserRoleByUserId(id);
    }
}
