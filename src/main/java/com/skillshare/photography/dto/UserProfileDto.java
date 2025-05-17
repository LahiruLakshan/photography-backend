package com.skillshare.photography.dto;

import java.time.LocalDateTime;
import java.util.List;

public class UserProfileDto {
    private Long id;
    private String username;
    private String email;
    private String profilePictureUrl;
    private LocalDateTime createdAt;
    private List<PhotographyPostDto> posts;
    private int followersCount;
    private int followingCount;

    // Default constructor
    public UserProfileDto() {
    }

    // All-args constructor
    public UserProfileDto(Long id, String username, String email, 
                         String profilePictureUrl, LocalDateTime createdAt,
                         List<PhotographyPostDto> posts, int followersCount,
                         int followingCount) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
        this.createdAt = createdAt;
        this.posts = posts;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<PhotographyPostDto> getPosts() {
        return posts;
    }

    public void setPosts(List<PhotographyPostDto> posts) {
        this.posts = posts;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    // Builder pattern implementation
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String username;
        private String email;
        private String profilePictureUrl;
        private LocalDateTime createdAt;
        private List<PhotographyPostDto> posts;
        private int followersCount;
        private int followingCount;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder profilePictureUrl(String profilePictureUrl) {
            this.profilePictureUrl = profilePictureUrl;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder posts(List<PhotographyPostDto> posts) {
            this.posts = posts;
            return this;
        }

        public Builder followersCount(int followersCount) {
            this.followersCount = followersCount;
            return this;
        }

        public Builder followingCount(int followingCount) {
            this.followingCount = followingCount;
            return this;
        }

        public UserProfileDto build() {
            return new UserProfileDto(id, username, email, profilePictureUrl,
                                    createdAt, posts, followersCount, followingCount);
        }
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "UserProfileDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                ", createdAt=" + createdAt +
                ", postsCount=" + (posts != null ? posts.size() : 0) +
                ", followersCount=" + followersCount +
                ", followingCount=" + followingCount +
                '}';
    }
}
