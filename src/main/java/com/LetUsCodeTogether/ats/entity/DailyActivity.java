package com.LetUsCodeTogether.ats.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.sql.Date;
import java.util.Calendar;

@Entity
@Table(name = "daily_activity")
public class DailyActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id", nullable = false)
    @JsonProperty
    private long activityId;

    @JsonProperty
    @Column(name = "user_id", nullable = false)
    private long userId;

    @JsonProperty
    @Column(name = "platform_id")
    private Integer platformId;

    @JsonProperty
    @Column(name = "problems_solved")
    private Long problemsSolved;

    @JsonProperty
    @Column(name = "contests_participated")
    private Long contestsParticipated;

    @JsonProperty
    @Column(name = "ratings")
    private Long ratings;

    @JsonProperty
    @Column(name = "points")
    private Double points;

    @JsonProperty
    @Column(name = "calculated_total_score")
    private Double calculatedTotalScore;

    @JsonProperty
    @Column(name = "previous_score")
    private Double previousScore;

    @JsonProperty
    @Column(name = "score_difference")
    private Double scoreDifference;

    @JsonProperty
    @Column(name = "streak_in_days")
    private Long streakInDays;

    @JsonProperty
    @Column(name = "overall_streak_in_days")
    private Long overallStreakInDays;

    @JsonProperty
    @Column(name = "created_by", nullable = false)
    private long createdBy;

    @JsonProperty
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdDate;

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
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

    public Long getProblemsSolved() {
        return problemsSolved;
    }

    public void setProblemsSolved(Long problemsSolved) {
        this.problemsSolved = problemsSolved;
    }

    public Long getContestsParticipated() {
        return contestsParticipated;
    }

    public void setContestsParticipated(Long contestsParticipated) {
        this.contestsParticipated = contestsParticipated;
    }

    public Long getRatings() {
        return ratings;
    }

    public void setRatings(Long ratings) {
        this.ratings = ratings;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public Double getCalculatedTotalScore() {
        return calculatedTotalScore;
    }

    public void setCalculatedTotalScore(Double calculatedTotalScore) {
        this.calculatedTotalScore = calculatedTotalScore;
    }

    public Double getPreviousScore() {
        return previousScore;
    }

    public void setPreviousScore(Double previousScore) {
        this.previousScore = previousScore;
    }

    public Double getScoreDifference() {
        return scoreDifference;
    }

    public void setScoreDifference(Double scoreDifference) {
        this.scoreDifference = scoreDifference;
    }

    public Long getStreakInDays() {
        return streakInDays;
    }

    public void setStreakInDays(Long streakInDays) {
        this.streakInDays = streakInDays;
    }

    public Long getOverallStreakInDays() {
        return overallStreakInDays;
    }

    public void setOverallStreakInDays(Long overallStreakInDays) {
        this.overallStreakInDays = overallStreakInDays;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public Calendar getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Calendar createdDate) {
        if (createdDate == null) {
            this.createdDate = Calendar.getInstance();
        } else {
            this.createdDate = createdDate;
        }
    }

    public DailyActivity() {
    }

    public DailyActivity(long userId, Date day, Long problemsSolved, Long contestsParticipated, Long ratings, Double points, Double calculatedTotalScore, Double previousScore, Double scoreDifference, Long streakInDays, Long overallStreakInDays) {
        this.userId = userId;
        this.problemsSolved = problemsSolved;
        this.createdDate = Calendar.getInstance();
        this.createdDate.setTime(day);
        this.contestsParticipated = contestsParticipated;
        this.ratings = ratings;
        this.points = points;
        this.calculatedTotalScore = calculatedTotalScore;
        this.previousScore = previousScore;
        this.scoreDifference = scoreDifference;
        this.streakInDays = streakInDays;
        this.overallStreakInDays = overallStreakInDays;
    }
}
