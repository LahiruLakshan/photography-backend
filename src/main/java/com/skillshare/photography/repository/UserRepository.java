package com.skillshare.photography.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillshare.photography.model.User;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByOauthProviderAndOauthId(String provider, String oauthId);
}