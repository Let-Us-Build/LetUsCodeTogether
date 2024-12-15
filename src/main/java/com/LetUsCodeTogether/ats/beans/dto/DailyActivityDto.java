package com.LetUsCodeTogether.ats.beans.dto;

import java.util.Date;

public class DailyActivityDto {
    private long userId;
    private Integer platformId;
    private Date day;
    private Long totalProblemsSolved;
    private Long totalContestsParticipated;
    private Long totalRatings;
    private Double totalPoints;
    private Double totalCalculatedScore;
    private Double totalScoreDifference;
    private Long latestStreakInDays;
    private Long latestOverallStreakInDays;

    public DailyActivityDto(long userId, Integer platformId, Date day, Long totalProblemsSolved, Long totalContestsParticipated,
                                Long totalRatings, Double totalPoints, Double totalCalculatedScore, Double totalScoreDifference,
                                Long latestStreakInDays, Long latestOverallStreakInDays) {
        this.userId = userId;
        this.platformId = platformId;
        this.day = day;
        this.totalProblemsSolved = totalProblemsSolved;
        this.totalContestsParticipated = totalContestsParticipated;
        this.totalRatings = totalRatings;
        this.totalPoints = totalPoints;
        this.totalCalculatedScore = totalCalculatedScore;
        this.totalScoreDifference = totalScoreDifference;
        this.latestStreakInDays = latestStreakInDays;
        this.latestOverallStreakInDays = latestOverallStreakInDays;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Long getTotalProblemsSolved() {
        return totalProblemsSolved;
    }

    public void setTotalProblemsSolved(Long totalProblemsSolved) {
        this.totalProblemsSolved = totalProblemsSolved;
    }

    public Long getTotalContestsParticipated() {
        return totalContestsParticipated;
    }

    public void setTotalContestsParticipated(Long totalContestsParticipated) {
        this.totalContestsParticipated = totalContestsParticipated;
    }

    public Long getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(Long totalRatings) {
        this.totalRatings = totalRatings;
    }

    public Double getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Double totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Double getTotalCalculatedScore() {
        return totalCalculatedScore;
    }

    public void setTotalCalculatedScore(Double totalCalculatedScore) {
        this.totalCalculatedScore = totalCalculatedScore;
    }

    public Double getTotalScoreDifference() {
        return totalScoreDifference;
    }

    public void setTotalScoreDifference(Double totalScoreDifference) {
        this.totalScoreDifference = totalScoreDifference;
    }

    public Long getLatestStreakInDays() {
        return latestStreakInDays;
    }

    public void setLatestStreakInDays(Long latestStreakInDays) {
        this.latestStreakInDays = latestStreakInDays;
    }

    public Long getLatestOverallStreakInDays() {
        return latestOverallStreakInDays;
    }

    public void setLatestOverallStreakInDays(Long latestOverallStreakInDays) {
        this.latestOverallStreakInDays = latestOverallStreakInDays;
    }

    public DailyActivityDto() {
    }
}
