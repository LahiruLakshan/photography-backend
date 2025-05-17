package com.skillshare.photography.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "follow_relationships")
public class FollowRelationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;
    
    @ManyToOne
    @JoinColumn(name = "followed_id")
    private User followed;
    
    // Default constructor
    public FollowRelationship() {
    }

    // Parameterized constructor
    public FollowRelationship(User follower, User followed) {
        this.follower = follower;
        this.followed = followed;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowed() {
        return followed;
    }

    public void setFollowed(User followed) {
        this.followed = followed;
    }

    // toString() method
    @Override
    public String toString() {
        return "FollowRelationship{" +
                "id=" + id +
                ", follower=" + (follower != null ? follower.getId() : null) +
                ", followed=" + (followed != null ? followed.getId() : null) +
                '}';
    }

    // equals() and hashCode() methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowRelationship that = (FollowRelationship) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}