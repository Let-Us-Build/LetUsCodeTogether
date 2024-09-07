package com.LetUsCodeTogether.ats.repo;

import com.LetUsCodeTogether.ats.entity.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlatformRepository extends JpaRepository<Platform, Integer> {
    List<Platform> findByPlatformName(String platformName);
}
