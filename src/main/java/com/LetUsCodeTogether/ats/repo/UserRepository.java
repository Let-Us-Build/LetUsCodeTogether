package com.LetUsCodeTogether.ats.repo;

import com.LetUsCodeTogether.ats.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllUsersByUsername(String username);
    List<User> findAllUsersByFirstNameOrLastName(String firstName, String lastName);

    Optional<User> findByUsernameOrEmail(String username, String email);
}
