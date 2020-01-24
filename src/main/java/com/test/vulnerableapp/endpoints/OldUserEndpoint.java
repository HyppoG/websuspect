package com.test.vulnerableapp.endpoints;

import java.net.URI;
import java.util.Optional;

import com.test.vulnerableapp.model.OldUser;
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
import com.test.vulnerableapp.operations.OldUserOperation;

@RestController
@RequestMapping("/api")
public class OldUserEndpoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(OldUserEndpoint.class);

    private OldUserOperation userService;

    @Autowired
    public OldUserEndpoint(OldUserOperation userService) {
        this.userService = userService;
    }
    
    @GetMapping("/olduser/{id}")
    public ResponseEntity<OldUser> getUser(@PathVariable long id) {
        LOGGER.debug("Getting user with id {}", id);

        try {
            return ResponseEntity.ok(userService.retrieveById(id).orElseThrow(RuntimeException::new));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @PostMapping("/olduser")
    public ResponseEntity<OldUser> addUser(@RequestBody OldUser user) {
        LOGGER.debug("Adding user {}", user);

        Optional<OldUser> addedUser = userService.addUser(user);
        if (addedUser.isPresent()) {
            LOGGER.debug("User added to database");
            return ResponseEntity
                    .created(URI.create("/olduser/" + user.getId()))
                    .body(addedUser.get());
        }

        return ResponseEntity.badRequest().build();
    }
    
    @RequestMapping(method = RequestMethod.PATCH, path = "/olduser")
    public ResponseEntity updateUser(@RequestBody OldUser user) {
        LOGGER.debug("Updating user {}", user.getId());

        Optional<OldUser> updatedUser = userService.updateUser(user);
        if (updatedUser.isPresent()) {
            return ResponseEntity.ok(updatedUser);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
