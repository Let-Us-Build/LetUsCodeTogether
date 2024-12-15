package com.LetUsCodeTogether.ats.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Calendar;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    @Column(name = "user_id", updatable = false)
    private Long userId;

    @JsonProperty
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @JsonProperty
    @Column(name = "first_name")
    private String firstName;

    @JsonProperty
    @Column(name = "last_name")
    private String lastName;

    @JsonProperty
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @JsonProperty
    @Column(name = "passcode")
    private String password;

    @JsonProperty
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdDate;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public User(Long userId, String username, String firstName, String lastName, String email, String password, Calendar createdDate) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.createdDate = createdDate;
    }

    public User() {
    }
}
