package com.LetUsCodeTogether.ats.beans.dto;

public class DailyActivityRequestDto {
    private long userId;
    private long platformId;
    private int year;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getPlatformId() {
        return platformId;
    }

    public void setPlatformId(long platformId) {
        this.platformId = platformId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public DailyActivityRequestDto() {
    }

    public DailyActivityRequestDto(long userId, long platformId, int year) {
        this.userId = userId;
        this.platformId = platformId;
        this.year = year;
    }
}
