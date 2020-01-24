package com.test.vulnerableapp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.vulnerableapp.model.UserInfo;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserInfo, Long>, UserRepositoryCustom {
    Optional<UserInfo> findByEmail(String email);
    Optional<UserInfo> findByUsername(String username);
    Optional<UserInfo> findById(long id);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);

    List<UserInfo> getAllByBirthday();
}
