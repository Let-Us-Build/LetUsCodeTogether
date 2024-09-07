package com.LetUsCodeTogether.ats.repo;

import com.LetUsCodeTogether.ats.entity.UserPlatformMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPlatformMappingRepository  extends JpaRepository<UserPlatformMapping, Integer> {
    Optional<UserPlatformMapping> findByUserIdAndPlatformId(int userId, int platformId);

    List<UserPlatformMapping> findAllByUserId(int userId);
}
