package com.LetUsCodeTogether.ats.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "contest")
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contest_id", nullable = false)
    @JsonProperty
    private long contestId;

    @Column(name = "contest_name", nullable = false)
    @JsonProperty
    private String contestName;

    @Column(name = "platform_id", nullable = false)
    @JsonProperty
    private long platformId;

    @Column(name = "type")
    @JsonProperty
    private String type;

    @Column(name = "platform_contest_id")
    @JsonProperty
    private long platformContestId;

    @Column(name = "phase")
    @JsonProperty
    private String phase;

    @Column(name = "frozen")
    @JsonProperty
    private String frozen;

    @Column(name = "duration")
    @JsonProperty
    private long duration;

    @Column(name = "start_time")
    @JsonProperty
    private String startTime;

    @Column(name = "end_time")
    @JsonProperty
    private String endTime;

    @Column(name = "contest_code")
    @JsonProperty
    private String contestCode;

    public Contest(long contestId, String contestName, long platformId, String type, long platformContestId, String phase, String frozen, long duration, String startTime, String endTime, String contestCode) {
        this.contestId = contestId;
        this.contestName = contestName;
        this.platformId = platformId;
        this.type = type;
        this.platformContestId = platformContestId;
        this.phase = phase;
        this.frozen = frozen;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = endTime;
        this.contestCode = contestCode;
    }

    public Contest() {
    }

    public long getContestId() {
        return contestId;
    }

    public void setContestId(long contestId) {
        this.contestId = contestId;
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public long getPlatformId() {
        return platformId;
    }

    public void setPlatformId(long platformId) {
        this.platformId = platformId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getPlatformContestId() {
        return platformContestId;
    }

    public void setPlatformContestId(long platformContestId) {
        this.platformContestId = platformContestId;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getFrozen() {
        return frozen;
    }

    public void setFrozen(String frozen) {
        this.frozen = frozen;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getContestCode() {
        return contestCode;
    }

    public void setContestCode(String contestCode) {
        this.contestCode = contestCode;
    }
}
