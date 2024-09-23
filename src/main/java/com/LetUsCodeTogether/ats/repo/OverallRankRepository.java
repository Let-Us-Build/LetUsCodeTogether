package com.LetUsCodeTogether.ats.repo;

import com.LetUsCodeTogether.ats.entity.OverallRank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OverallRankRepository extends JpaRepository<OverallRank, Integer> {
    Optional<OverallRank> findByUserId(long userId);
}
