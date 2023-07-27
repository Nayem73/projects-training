package com.javafest.aifarming.controller;

import com.javafest.aifarming.model.UserInfo;
import com.javafest.aifarming.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserInfoController {
    private final UserInfoRepository userInfoRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserInfoController(UserInfoRepository userInfoRepository, PasswordEncoder passwordEncoder) {
        this.userInfoRepository = userInfoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        Optional<UserInfo> existingUser = userInfoRepository.findByUserName(userInfo.getUserName());
        Optional<UserInfo> existingUserByEmail = userInfoRepository.findByEmail(userInfo.getEmail());
        if (existingUser.isPresent()) {
            // User already exists, return an error message
            return "Error: User already exists!";
        } else if (existingUserByEmail.isPresent()) {
            //User with this email already exists
            return "Error: User with this email already exists";
        }
        else {
            // Encode the password and save the new user
            userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
            userInfoRepository.save(userInfo);
            return "user added successfully";
        }
    }

    @PostMapping("/login")
    public String login(@RequestBody UserInfo loginRequest) {
        String username = loginRequest.getUserName();
        String password = loginRequest.getPassword();

        // Retrieve the user with the provided username from the database
        UserInfo user = userInfoRepository.findByUserName(username).orElse(null);

        if (user != null) {
            // Check if the password matches
            if (passwordEncoder.matches(password, user.getPassword())) {
                // Password is correct, log in the user
                return "Login successful!";
            } else {
                // Incorrect password
                return "Error: Incorrect password!";
            }
        } else {
            // User does not exist
            return "Error: User not found!";
        }
    }
}
