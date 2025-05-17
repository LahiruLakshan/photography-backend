package com.skillshare.photography.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String oauthProvider;
    private String oauthId;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    private String email;
    private String profilePictureUrl;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}