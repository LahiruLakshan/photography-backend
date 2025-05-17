package com.skillshare.photography.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.skillshare.photography.dto.PhotographyPostDto;
import com.skillshare.photography.dto.PhotographyPostRequest;
import com.skillshare.photography.dto.UserProfileDto;
import com.skillshare.photography.service.PhotographyPostService;
import com.skillshare.photography.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/photography")
public class PhotographyController {

    private final PhotographyPostService postService;
    private final UserService userService;
    
    public PhotographyController(PhotographyPostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }
    
    // Get current user's profile
    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getCurrentUserProfile(@AuthenticationPrincipal OAuth2User principal) {
        UserProfileDto profile = userService.getCurrentUserProfile(principal);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileDto> updateProfile(
            @RequestBody UserProfileDto profileDto,
            @AuthenticationPrincipal OAuth2User principal) {
        UserProfileDto updatedProfile = userService.updateProfile(profileDto, principal);
        return ResponseEntity.ok(updatedProfile);
    }
    
    // 1. GET - Get all photography posts
    @GetMapping("/posts")
    public ResponseEntity<List<PhotographyPostDto>> getAllPosts() {
        List<PhotographyPostDto> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }
    
    // 2. POST - Create a new photography post
    @PostMapping("/posts")
    public ResponseEntity<PhotographyPostDto> createPost(
            @RequestPart("post") @Valid PhotographyPostRequest postRequest,
            @RequestPart("files") MultipartFile[] files,
            @AuthenticationPrincipal OAuth2User principal) {
        
        PhotographyPostDto createdPost = postService.createPost(postRequest, files, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }
    
    // 3. PUT - Update a photography post
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PhotographyPostDto> updatePost(
            @PathVariable Long postId,
            @RequestBody @Valid PhotographyPostRequest postRequest,
            @AuthenticationPrincipal OAuth2User principal) {
        
        PhotographyPostDto updatedPost = postService.updatePost(postId, postRequest, principal);
        return ResponseEntity.ok(updatedPost);
    }
    
    // 4. DELETE - Delete a photography post
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal OAuth2User principal) {
        
        postService.deletePost(postId, principal);
        return ResponseEntity.noContent().build();
    }
}