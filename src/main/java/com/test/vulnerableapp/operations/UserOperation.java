package com.test.vulnerableapp.operations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.owasp.esapi.ESAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.vulnerableapp.model.UserInfo;
import com.test.vulnerableapp.repositories.UserRepository;

@Service
public class UserOperation {
    private UserRepository userRepository;

    @Autowired
    public UserOperation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserInfo> retrieveById(long id) {
        return userRepository.findById(id);
    }
    public Optional<UserInfo> retrieveByUsername(String username) { return userRepository.findByUsername(username); }

    public List<UserInfo> retrieveAll() {
        return userRepository.findAll();
    }

    public Boolean isUserExistsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
    public Boolean isUserExistsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public void deleteUserById(long id) {
        userRepository.deleteById(id);
    }

    public Optional<UserInfo>  addUser(UserInfo user) {

        // TODO : Fix validation, move it

        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            String message = "The username '" + ESAPI.encoder().encodeForHTML(user.getUsername())
                    + "' is not valid or already registered.";
            //throw new CommonsException(message);
            return Optional.empty();
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()){
            String message = "The email address '" + ESAPI.encoder().encodeForHTML(user.getEmail())
                    + "' is not valid or already registered.";
            //throw new CommonsException(message);
            return Optional.empty();
        }

        if (user.isValid()) {
            return Optional.of(userRepository.saveAndFlush(user));
        }

        return Optional.empty();
    }

    public Optional<UserInfo> updateUser(UserInfo user) {
        Optional<UserInfo> result = retrieveById(user.getId());

        if (result.isPresent()) {
        	UserInfo retrievedUser = result.get();
            retrievedUser.setEmail(user.getEmail());
            retrievedUser.setUsername(user.getUsername());
            retrievedUser.setBirthday(user.getBirthday());

            return Optional.of(userRepository.saveAndFlush(retrievedUser));
        }

        return Optional.empty();
    }

    public String getUserListByTodayDate() {

        Long count = Long.valueOf(0);
        LocalDateTime date = LocalDateTime.now();
        int today_month = date.getMonthValue();
        int today_day = date.getDayOfMonth();

        List<UserInfo> userList = userRepository.getAllByBirthday();

        //List<UserInfo> userList = userdao.getUserListByTodayDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/u");
        String today = LocalDateTime.now().format(formatter);
        String birthday = "Today (" + today + ") " + "<b>" + userList.size() + "</b> of our customer(s)";
        int i = 0;

        for(UserInfo user: userList) {
            if(i == 0){
                birthday += " (";
            }
            if(i++ == userList.size() - 1){
                birthday += "<b>" + user.getUsername() + "</b>)";
            } else {
                birthday += "<b>" + user.getUsername() + "</b> & ";
            }
        }

        birthday += " are celebrating their birthday!";

        return birthday;
    }
}
