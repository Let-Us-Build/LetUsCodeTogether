package com.LetUsCodeTogether.ats.service;

import com.LetUsCodeTogether.ats.entity.User;
import com.LetUsCodeTogether.ats.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        user.setCreatedDate(null);
        return  userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getUsersByUsernameOrName(String usernameOrName){
        List<User> users = new ArrayList<>();
        List<User> usersByUsername = userRepository.findAllUsersByUsername(usernameOrName);
        List<User> usersByName = userRepository.findAllUsersByFirstNameOrLastName(usernameOrName, usernameOrName);
        users.addAll(usersByUsername);
        users.addAll(usersByName);
        users = users.stream()
                .distinct()
                .collect(Collectors.toList());
        return users;
    }

    public String deleteUser(Long userId) {
        userRepository.deleteById(userId);
        return "User deleted successfully \n User Id: !! " + userId;
    }

    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getUserId()).orElse(null);
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        return userRepository.save(existingUser);
    }

    public User getUsersByEmail(String email) {
        return userRepository.findByUsernameOrEmail(email, email).orElse(null);
    }
}
