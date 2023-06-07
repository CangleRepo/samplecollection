package com.ljzh.samplecollection.service;

import com.ljzh.samplecollection.domain.entity.Role;
import com.ljzh.samplecollection.domain.vo.RoleVO;
import com.ljzh.samplecollection.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    // 新增角色
    public Role saveRole(RoleVO roleVO) {
        Role role = new Role();
        role.setName(roleVO.getName());
        role.setCode(roleVO.getCode());
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        return roleRepository.save(role);
    }

    // 根据id删除角色
    public void deleteRoleById(Long id) {
        roleRepository.deleteById(id);
    }

    // 更新角色信息
    public void updateRole(Role role) {
        roleRepository.save(role);
    }

    // 根据id查询角色
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    // 查询所有角色
    public List<RoleVO> list() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(role -> new RoleVO(
                role.getId(), role.getName(), role.getCode(),
                role.getCreateTime(), role.getUpdateTime()
        )).collect(Collectors.toList());
    }


    public Page<Role> getAllRolesByPage(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        return roleRepository.findAll(pageable);
    }

    public boolean isRoleNameExists(String name) {
        return roleRepository.existsByName(name);
    }
}

