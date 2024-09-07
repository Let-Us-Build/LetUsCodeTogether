package com.LetUsCodeTogether.ats.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Calendar;

@Entity
@Table(name = "user_platform_mapping")
public class UserPlatformMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    @Column(name = "user_platform_id", updatable = false)
    private Integer userPlatformId;

    @JsonProperty
    @Column(name = "user_id")
    private Integer userId;

    @JsonProperty
    @Column(name = "platform_id")
    private Integer platformId;

    @JsonProperty
    @Column(name = "username_on_platform")
    private String usernameOnPlatform;

    @JsonProperty
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdDate;

    public Integer getUserPlatformId() {
        return userPlatformId;
    }

    public void setUserPlatformId(Integer userPlatformId) {
        this.userPlatformId = userPlatformId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public String getUsernameOnPlatform() {
        return usernameOnPlatform;
    }

    public void setUsernameOnPlatform(String usernameOnPlatform) {
        this.usernameOnPlatform = usernameOnPlatform;
    }

    public Calendar getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Calendar createdDate) {
        if (createdDate.equals(null)) {
            this.createdDate = Calendar.getInstance();
        } else {
            this.createdDate = createdDate;
        }
    }

    public UserPlatformMapping(Integer userPlatformId, Integer userId, Integer platformId, String usernameOnPlatform, Calendar createdDate) {
        this.userPlatformId = userPlatformId;
        this.userId = userId;
        this.platformId = platformId;
        this.usernameOnPlatform = usernameOnPlatform;
        this.createdDate = createdDate;
    }

    public UserPlatformMapping() {
    }
}
