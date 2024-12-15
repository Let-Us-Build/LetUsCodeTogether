package com.LetUsCodeTogether.ats.controller;

import com.LetUsCodeTogether.ats.beans.dto.Login;
import com.LetUsCodeTogether.ats.beans.dto.SignupDto;
import com.LetUsCodeTogether.ats.entity.User;
import com.LetUsCodeTogether.ats.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class LoginController {
    @Autowired
    private LoginService loginService;
    @PostMapping("/login")
    public Map<String, Object> loginUser(@RequestBody Login login) {
        return loginService.validateUserCreds(login);
    }

    @PostMapping("/signup")
    public User signupUser(@RequestBody SignupDto signupDto) {
        return loginService.signup(signupDto);
    }
}
