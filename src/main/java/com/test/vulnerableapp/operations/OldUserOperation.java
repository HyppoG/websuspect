package com.test.vulnerableapp.operations;

import java.util.Optional;

import com.test.vulnerableapp.model.OldUser;
import com.test.vulnerableapp.repositories.OldUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.vulnerableapp.model.UserInfo;
import com.test.vulnerableapp.repositories.UserRepository;

@Service
public class OldUserOperation {
    private OldUserRepository userRepository;

    @Autowired
    public OldUserOperation(OldUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<OldUser> retrieveById(long id) {
        return userRepository.findById(id);
    }
    public String retrieveByUsername(String username) { return userRepository.findByUsername(username); }

    public Optional<OldUser> addUser(OldUser user) {
        if (user.isValid() && !userRepository.findByEmail(user.getEmail()).isPresent()) {
            return Optional.of(userRepository.saveAndFlush(user));
        }

        return Optional.empty();
    }

    public Optional<OldUser> updateUser(OldUser user) {
        Optional<OldUser> result = retrieveById(user.getId());

        if (result.isPresent()) {
            OldUser retrievedUser = result.get();
            retrievedUser.setEmail(user.getEmail());
            retrievedUser.setUsername(user.getUsername());
            retrievedUser.setBirthday(user.getBirthday());

            return Optional.of(userRepository.saveAndFlush(retrievedUser));
        }

        return Optional.empty();
    }

    public Boolean isValidUserPassword(String username, String password) {
        return userRepository.existsByUsernameAndPassword(username, password);
    }
}
