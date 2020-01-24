package com.test.vulnerableapp.repositories;

import com.test.vulnerableapp.model.OldUser;
import com.test.vulnerableapp.model.UserInfo;

import java.util.Optional;

public interface OldUserRepositoryCustom {
    String findByUsername(String username);
    Boolean existsByUsernameAndPassword(String username, String password);

}
