package com.LetUsCodeTogether.ats.controller;

import com.LetUsCodeTogether.ats.entity.User;
import com.LetUsCodeTogether.ats.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/users/add")
    public User addUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/users/")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{usernameOrName}")
    public List<User> getUsersByUsernameOrName(@PathVariable String usernameOrName) {
        return userService.getUsersByUsernameOrName(usernameOrName);
    }

    @GetMapping("/users/email/{email}")
    public User getUsersByEmail(@PathVariable String email) {
        return userService.getUsersByEmail(email);
    }

    @PutMapping("/users/update")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/users/delete/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId);
    }
}
