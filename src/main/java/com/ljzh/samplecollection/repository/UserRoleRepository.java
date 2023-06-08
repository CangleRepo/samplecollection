package com.ljzh.samplecollection.repository;

import com.ljzh.samplecollection.domain.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findUserRoleByUserId(Long id);

    void deleteByUserId(Long id);

    List<UserRole> findUserRoleByRoleId(Long roleId);

    UserRole findUserRoleByRoleIdAndUserId(Long roleId, Long id);
}
