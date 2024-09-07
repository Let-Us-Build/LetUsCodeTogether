package com.LetUsCodeTogether.ats.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "platforms")
public class Platform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    @Column(name = "platform_id", updatable = false)
    private Integer platformId;

    @JsonProperty
    @Column(name = "platform_name")
    private String platformName;

    @JsonProperty
    @Column(name = "platform_url")
    private String platformUrl;

    @JsonProperty
    @Column(name = "platform_status")
    private String platformStatus;

    @JsonProperty
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdDate;

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public String getPlatformUrl() {
        return platformUrl;
    }

    public void setPlatformUrl(String platformUrl) {
        this.platformUrl = platformUrl;
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

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(String platformStatus) {
        this.platformStatus = platformStatus;
    }

    public Platform(Integer platformId, String platformName, String platformUrl, Calendar createdDate, String platformStatus) {
        this.platformId = platformId;
        this.platformName = platformName;
        this.platformUrl = platformUrl;
        this.createdDate = createdDate;
        this.platformStatus = platformStatus;
    }

    public Platform() {
    }
}
