package com.LetUsCodeTogether.ats.repo;

import com.LetUsCodeTogether.ats.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ScoreRepository extends JpaRepository<Score, Integer> {
    Score findByUserIdAndPlatformId(long userId, int platformId);

    List<Score> findAllByPlatformId(int platformId);

    List<Score> findAllByUserId(int userId);
}
