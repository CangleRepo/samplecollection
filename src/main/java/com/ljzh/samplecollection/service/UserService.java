package com.ljzh.samplecollection.service;

import com.ljzh.samplecollection.domain.entity.Role;
import com.ljzh.samplecollection.domain.entity.User;
import com.ljzh.samplecollection.domain.entity.UserRole;
import com.ljzh.samplecollection.domain.vo.UserVO;
import com.ljzh.samplecollection.repository.RoleRepository;
import com.ljzh.samplecollection.repository.UserRepository;
import com.ljzh.samplecollection.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    // 新增用户
    public void addUser(User user) {
        userRepository.save(user);
    }

    // 根据id删除用户
    @Transactional(rollbackOn = Exception.class)
    public void deleteUserById(Long id) {
        userRoleRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }

    // 更新用户信息
    public void updateUser(User user) {
        userRepository.save(user);
    }

    // 根据id查询用户
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // 查询所有用户
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findUserByUsername(String username){
        return userRepository.findUserByUsername(username);
    }

    public Page<User> getAllUsersByPage(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        return userRepository.findAll(pageable);
    }

    public User saveUserWithRoles(UserVO userVO) {
        User user = new User();
        user.setUsername(userVO.getUsername());
        user.setPassword(userVO.getPassword());
        user.setCode(userVO.getCode());
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        User savedUser = userRepository.save(user);

        List<Role> roles = roleRepository.findAllById(userVO.getRoleIds());
        List<UserRole> userRoles = new ArrayList<>();
        for (Role role : roles) {
            UserRole userRole = new UserRole();
            userRole.setUserId(savedUser.getId());
            userRole.setRoleId(role.getId());
            userRole.setCreateTime(LocalDateTime.now());
            userRole.setUpdateTime(LocalDateTime.now());
            userRoles.add(userRole);
        }
        userRoleRepository.saveAll(userRoles);

        return savedUser;
    }
}

