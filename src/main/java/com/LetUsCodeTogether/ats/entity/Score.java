package com.LetUsCodeTogether.ats.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Calendar;

@Entity
@Table(name = "score")
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    @Column(name = "score_id", updatable = false)
    private int scoreId;

    @JsonProperty
    @Column(name = "user_id")
    private long userId;

    @JsonProperty
    @Column(name = "platform_id")
    private Integer platformId;

    @JsonProperty
    @Column(name = "no_of_problems_solved")
    private int noOfProblemsSolved;

    @JsonProperty
    @Column(name = "no_of_contests")
    private int noOfContests;

    @JsonProperty
    @Column(name = "luct_rank")
    private int luctRank;

    @JsonProperty
    @Column(name = "ratings")
    private double ratings;

    @JsonProperty
    @Column(name = "points")
    private double points;

    @JsonProperty
    @Column(name = "calculated_total_score")
    private double calculatedTotalScore;

    @JsonProperty
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdDate;

    public int getScoreId() {
        return scoreId;
    }

    public void setScoreId(int scoreId) {
        this.scoreId = scoreId;
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

    public int getNoOfProblemsSolved() {
        return noOfProblemsSolved;
    }

    public void setNoOfProblemsSolved(int noOfProblemsSolved) {
        this.noOfProblemsSolved = noOfProblemsSolved;
    }

    public int getNoOfContests() {
        return noOfContests;
    }

    public void setNoOfContests(int noOfContests) {
        this.noOfContests = noOfContests;
    }

    public double getRatings() {
        return ratings;
    }

    public void setRatings(double ratings) {
        this.ratings = ratings;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public double getCalculatedTotalScore() {
        return calculatedTotalScore;
    }

    public void setCalculatedTotalScore(double calculatedTotalScore) {
        this.calculatedTotalScore = calculatedTotalScore;
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

    public int getLuctRank() {
        return luctRank;
    }

    public void setLuctRank(int luctRank) {
        this.luctRank = luctRank;
    }

    public Score(int scoreId, long userId, Integer platformId, int noOfProblemsSolved, int noOfContests, double ratings, double points, int calculatedTotalScore, Calendar createdDate, int luctRank) {
        this.scoreId = scoreId;
        this.userId = userId;
        this.platformId = platformId;
        this.noOfProblemsSolved = noOfProblemsSolved;
        this.noOfContests = noOfContests;
        this.ratings = ratings;
        this.points = points;
        this.calculatedTotalScore = calculatedTotalScore;
        this.createdDate = createdDate;
        this.luctRank = luctRank;
    }

    public Score() {
    }
}
