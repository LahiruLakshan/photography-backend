package com.skillshare.photography.service;

import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillshare.photography.dto.PhotographyPostDto;
import com.skillshare.photography.dto.UserProfileDto;
import com.skillshare.photography.model.PhotographyPost;
import com.skillshare.photography.model.User;
import com.skillshare.photography.oauth.UserPrincipal;
import com.skillshare.photography.repository.FollowRelationshipRepository;
import com.skillshare.photography.repository.UserRepository;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PhotographyPostService photographyPostService;
    private final FollowRelationshipRepository followRelationshipRepository;

    public UserService(UserRepository userRepository,
                     @Lazy PhotographyPostService photographyPostService,
                     FollowRelationshipRepository followRelationshipRepository) {
        this.userRepository = userRepository;
        this.photographyPostService = photographyPostService;
        this.followRelationshipRepository = followRelationshipRepository;
    }

    /**
     * Gets or creates a user from OAuth2 authentication
     */
    public User getOrCreateUserFromOAuth(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");
        
        // Try to find existing user by email
        Optional<User> existingUser = userRepository.findByEmail(email);
        
        if (existingUser.isPresent()) {
            return existingUser.get();
        }
        
        // Create new user
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setUsername(name);
        newUser.setProfilePictureUrl(picture);
        
        // Set provider-specific fields
        if (oAuth2User instanceof UserPrincipal) {
            UserPrincipal principal = (UserPrincipal) oAuth2User;
            newUser.setOauthProvider(principal.getAttributes().get("iss").toString());
            newUser.setOauthId(principal.getAttributes().get("sub").toString());
        }
        
        return userRepository.save(newUser);
    }

    /**
     * Gets the complete profile of the currently authenticated user
     */
    public UserProfileDto getCurrentUserProfile(OAuth2User principal) {
        User user = getOrCreateUserFromOAuth(principal);
        
        return UserProfileDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profilePictureUrl(user.getProfilePictureUrl())
                .createdAt(user.getCreatedAt())
                .posts(photographyPostService.getPostsByUser(user))
                .followersCount(getFollowersCount(user))
                .followingCount(getFollowingCount(user))
                .build();
    }
    /**
     * Gets the currently authenticated user entity
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof UserPrincipal) {
            String email = ((UserPrincipal) principal).getEmail();
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
        
        return null;
    }

    /**
     * Gets user by ID
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Converts PhotographyPost entity to DTO
     */
    private PhotographyPostDto convertToPostDto(PhotographyPost post) {
        PhotographyPostDto dto = new PhotographyPostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setDescription(post.getDescription());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        
        // Set media URLs if needed
        // dto.setMediaUrls(...);
        
        return dto;
    }

    /**
     * Gets follower count for a user (stub implementation)
     */
 private int getFollowersCount(User user) {
        return followRelationshipRepository.countFollowers(user.getId());
    }

    private int getFollowingCount(User user) {
        return followRelationshipRepository.countFollowing(user.getId());
    }

    /**
     * Updates user profile information
     */
    public UserProfileDto updateProfile(UserProfileDto profileDto, OAuth2User principal) {
        User user = getOrCreateUserFromOAuth(principal);
        
        // Update allowed fields
        if (profileDto.getUsername() != null) {
            user.setUsername(profileDto.getUsername());
        }
        if (profileDto.getProfilePictureUrl() != null) {
            user.setProfilePictureUrl(profileDto.getProfilePictureUrl());
        }
        
        User updatedUser = userRepository.save(user);
        
        return getCurrentUserProfile(principal);
    }
}