package com.ljzh.samplecollection.repository;

import com.ljzh.samplecollection.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);

    List<User> findByIdIn(List<Long> collect);
}
