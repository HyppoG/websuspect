package com.test.vulnerableapp.endpoints;

import java.net.URI;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.test.vulnerableapp.model.UserInfo;
import com.test.vulnerableapp.operations.UserOperation;

@RestController
@RequestMapping("/api")
public class UserEndpoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserEndpoint.class);

    private UserOperation userService;

    @Autowired
    public UserEndpoint(UserOperation userService) {
        this.userService = userService;
    }
    
    @GetMapping("/user/{id}")
    public ResponseEntity<UserInfo> getUser(@PathVariable long id) {
        LOGGER.debug("Getting user with id {}", id);

        try {
            return ResponseEntity.ok(userService.retrieveById(id).orElseThrow(RuntimeException::new));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @PostMapping("/user")
    public ResponseEntity<UserInfo> addUser(@RequestBody UserInfo user) {
        LOGGER.debug("Adding user {}", user);

        Optional<UserInfo> addedUser = userService.addUser(user);
        if (addedUser.isPresent()) {
            LOGGER.debug("User added to database");
            return ResponseEntity
                    .created(URI.create("/user/" + user.getId()))
                    .body(addedUser.get());
        }

        return ResponseEntity.badRequest().build();
    }
    
    @RequestMapping(method = RequestMethod.PATCH, path = "/user")
    public ResponseEntity updateUser(@RequestBody UserInfo user) {
        LOGGER.debug("Updating user {}", user.getId());

        Optional<UserInfo> updatedUser = userService.updateUser(user);
        if (updatedUser.isPresent()) {
            return ResponseEntity.ok(updatedUser);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
