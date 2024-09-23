package com.LetUsCodeTogether.ats.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ApiScheduler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    @Column(name = "id")
    private Long id;

    @JsonProperty
    @Column(name = "api_url")
    private String apiUrl;

    @JsonProperty
    @Column(name = "payload")
    private String payload;

    @JsonProperty
    @Column(name = "cron_expression")
    private String cronExpression;

    @JsonProperty
    @Column(name = "last_execution")
    private LocalDateTime lastExecution;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public LocalDateTime getLastExecution() {
        return lastExecution;
    }

    public void setLastExecution(LocalDateTime lastExecution) {
        this.lastExecution = lastExecution;
    }
}
