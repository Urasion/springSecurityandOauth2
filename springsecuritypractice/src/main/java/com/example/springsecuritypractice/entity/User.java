package com.example.springsecuritypractice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;

    private String username;

    private String password;

    private String email;

    private String role; // USER, ADMIN, MANAGER

    private LocalDateTime createDate;

    private String provider;

    private String providerId;

    @Builder
    public User(String username, String password, String email, String role, LocalDateTime createDate, String provider, String providerId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.createDate = createDate;
        this.provider = provider;
        this.providerId = providerId;
    }
}
