package com.skillshare.photography.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.skillshare.photography.model.FollowRelationship;

public interface FollowRelationshipRepository extends JpaRepository<FollowRelationship, Long> {
    @Query("SELECT COUNT(f) FROM FollowRelationship f WHERE f.followed.id = :userId")
    int countFollowers(Long userId);
    
    @Query("SELECT COUNT(f) FROM FollowRelationship f WHERE f.follower.id = :userId")
    int countFollowing(Long userId);
}
