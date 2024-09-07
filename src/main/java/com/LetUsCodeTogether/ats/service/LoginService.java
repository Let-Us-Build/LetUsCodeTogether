package com.LetUsCodeTogether.ats.service;

import com.LetUsCodeTogether.ats.beans.dto.Login;
import com.LetUsCodeTogether.ats.entity.User;
import com.LetUsCodeTogether.ats.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {
    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> validateUserCreds(Login login) {
        User user = userRepository.findByUsernameOrEmail(login.getUsernameOrEmail(), login.getUsernameOrEmail())
                        .orElse(null);
        Map<String, Object> response = new HashMap<>();
        if(user != null) {
            if (user.getPassword().equals(login.getPassword())) {
                response.put("status", "success");
                response.put("user", user);
            } else {
                response.put("status", "Invalid Credentials");
            }
        } else {
            response.put("status", "No user exists with username: "+login.getUsernameOrEmail() +". Please reenter username again.");
        }
        return response;
    }
}
