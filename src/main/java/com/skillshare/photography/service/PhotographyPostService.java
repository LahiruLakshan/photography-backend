package com.skillshare.photography.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.skillshare.photography.dto.PhotographyPostDto;
import com.skillshare.photography.dto.PhotographyPostRequest;
import com.skillshare.photography.enums.MediaType;
import com.skillshare.photography.exception.ResourceNotFoundException;
import com.skillshare.photography.model.PhotographyPost;
import com.skillshare.photography.model.PostMedia;
import com.skillshare.photography.model.User;
import com.skillshare.photography.repository.PhotographyPostRepository;
import com.skillshare.photography.repository.PostMediaRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class PhotographyPostService {

    private final PhotographyPostRepository postRepository;
    private final PostMediaRepository mediaRepository;
    private final UserService userService;
    private final FileStorageService fileStorageService;
    private PhotographyPostRepository photographyPostRepository;
    
    public List<PhotographyPostDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public PhotographyPostDto createPost(PhotographyPostRequest request, MultipartFile[] files, OAuth2User principal) {
        User currentUser = userService.getOrCreateUserFromOAuth(principal);
        
        // Validate media files
        if (files.length > 3) {
            throw new IllegalArgumentException("Maximum 3 media files allowed per post");
        }
        
        PhotographyPost post = new PhotographyPost();
        post.setUser(currentUser);
        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        
        PhotographyPost savedPost = postRepository.save(post);
        
        // Process and save media files
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            String mediaUrl = fileStorageService.storeFile(file);
            
            PostMedia media = new PostMedia();
            media.setPost(savedPost);
            media.setMediaUrl(mediaUrl);
            media.setMediaType(file.getContentType().startsWith("video") ? MediaType.VIDEO : MediaType.IMAGE);
            media.setDisplayOrder(i);
            
            mediaRepository.save(media);
        }
        
        return convertToDto(savedPost);
    }
    
    public PhotographyPostDto updatePost(Long postId, PhotographyPostRequest request, OAuth2User principal) {
        User currentUser = userService.getOrCreateUserFromOAuth(principal);
        PhotographyPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        
        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only update your own posts");
        }
        
        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        
        PhotographyPost updatedPost = postRepository.save(post);
        return convertToDto(updatedPost);
    }
    
    public void deletePost(Long postId, OAuth2User principal) {
        User currentUser = userService.getOrCreateUserFromOAuth(principal);
        PhotographyPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        
        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only delete your own posts");
        }
        
        // Delete media files first
        post.getMedia().forEach(media -> fileStorageService.deleteFile(media.getMediaUrl()));
        
        postRepository.delete(post);
    }
    
    private PhotographyPostDto convertToDto(PhotographyPost post) {
        PhotographyPostDto dto = new PhotographyPostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setDescription(post.getDescription());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        dto.setAuthorUsername(post.getUser().getUsername());
        dto.setAuthorProfilePicture(post.getUser().getProfilePictureUrl());
        
        dto.setMediaUrls(post.getMedia().stream()
                .map(PostMedia::getMediaUrl)
                .collect(Collectors.toList()));
        
        return dto;
    }

    /**
     * Gets all posts for a specific user
     */
    public List<PhotographyPostDto> getPostsByUser(User user) {
        return photographyPostRepository.findByUser(user)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
