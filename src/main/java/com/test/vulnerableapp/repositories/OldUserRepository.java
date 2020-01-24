package com.test.vulnerableapp.repositories;

import com.test.vulnerableapp.model.OldUser;
import com.test.vulnerableapp.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OldUserRepository extends JpaRepository<OldUser, Long>, OldUserRepositoryCustom {
    Optional<OldUser> findByEmail(String email);
    String findByUsername(String username);
    Boolean existsByUsernameAndPassword(String username, String password);
    Optional<OldUser> findById(long id);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);

}
