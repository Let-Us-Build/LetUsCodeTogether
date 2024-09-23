package com.LetUsCodeTogether.ats.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Calendar;

@Entity
@Table(name = "overall_ranking")
public class OverallRank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    @Column(name = "overall_rank_id", updatable = false)
    private int overallRankId;

    @JsonProperty
    @Column(name = "user_id")
    private long userId;

    @JsonProperty
    @Column(name = "total_score")
    private Double totalScore;

    @JsonProperty
    @Column(name = "ranking")
    private int rank;

    @JsonProperty
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdDate;

    public int getOverallRankId() {
        return overallRankId;
    }

    public void setOverallRankId(int overallRankId) {
        this.overallRankId = overallRankId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
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

    public OverallRank() {
    }

    public OverallRank(int overallRankId, Integer userId, Double totalScore, int rank, Calendar createdDate) {
        this.overallRankId = overallRankId;
        this.userId = userId;
        this.totalScore = totalScore;
        this.rank = rank;
        this.createdDate = createdDate;
    }
}
