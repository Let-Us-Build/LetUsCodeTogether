package com.LetUsCodeTogether.ats.repo;

import com.LetUsCodeTogether.ats.beans.dto.DailyActivityDto;
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
    DailyActivity findTopByUserIdAndPlatformIdOrderByCreatedDateDesc(long userId, int platformId);
    DailyActivity findTopByUserIdOrderByCreatedDateDesc(long userId);

    @Query("SELECT new com.LetUsCodeTogether.ats.entity.DailyActivity( " +
            "da.userId, DATE(da.createdDate) as day, " +
            "SUM(da.problemsSolved) as problemsSolved, " +
            "SUM(da.contestsParticipated) as contestsParticipated, " +
            "SUM(da.ratings) as ratings, " +
            "SUM(da.points) as points, " +
            "SUM(da.calculatedTotalScore) as calculatedTotalScore, " +
            "MAX(da.previousScore) as previousScore, " +
            "SUM(da.scoreDifference) as scoreDifference, " +
            "MAX(da.streakInDays) as streakInDays, " +
            "MAX(da.overallStreakInDays) as overallStreakInDays " +
            ") " +
            "FROM DailyActivity da " +
            "WHERE da.userId = :userId AND YEAR(da.createdDate) = :year " +
            "GROUP BY da.userId, DATE(da.createdDate) " +
            "ORDER BY DATE(da.createdDate) ASC")
    List<DailyActivity> findAllByUserIdAndYear(@Param("userId") long userId, @Param("year") int year);

//    @Query("SELECT new com.LetUsCodeTogether.ats.dto.DailyActivityDto( " +
//            "da.userId, da.platformId, DATE(da.createdDate) as day, " +
//            "SUM(da.problemsSolved) as totalProblemsSolved, " +
//            "SUM(da.contestsParticipated) as totalContestsParticipated, " +
//            "SUM(da.ratings) as totalRatings, " +
//            "SUM(da.points) as totalPoints, " +
//            "SUM(da.calculatedTotalScore) as totalCalculatedScore, " +
//            "SUM(da.scoreDifference) as totalScoreDifference, " +
//            "MAX(da.streakInDays) as latestStreakInDays, " +
//            "MAX(da.overallStreakInDays) as latestOverallStreakInDays " +
//            ") " +
//            "FROM DailyActivity da " +
//            "WHERE da.userId = :userId AND YEAR(da.createdDate) = :year " +
//            "GROUP BY da.userId, da.platformId, DATE(da.createdDate) " +
//            "ORDER BY DATE(da.createdDate) ASC")
//    List<DailyActivityDto> findAllDailyActivityDtoByUserIdAndYear(long userId, int year);

}
