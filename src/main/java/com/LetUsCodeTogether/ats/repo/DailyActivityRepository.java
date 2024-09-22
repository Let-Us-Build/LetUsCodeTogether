package com.LetUsCodeTogether.ats.repo;

import com.LetUsCodeTogether.ats.entity.DailyActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;

@Repository
public interface DailyActivityRepository extends JpaRepository<DailyActivity, Long> {

    @Query("SELECT da FROM DailyActivity da WHERE (:platformId IS NULL OR da.platformId = :platformId) AND da.createdDate = :activityDate AND da.userId = :userId")
    List<DailyActivity> findByCreatedDateAndUserIdAndPlatformId(
            @Param("activityDate") Calendar activityDate,
            @Param("userId") long userId,
            @Param("platformId") Integer platformId
    );

}
