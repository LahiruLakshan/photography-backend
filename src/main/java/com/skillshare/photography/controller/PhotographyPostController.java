package com.skillshare.photography.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillshare.photography.model.PhotographyPost;
import com.skillshare.photography.service.PhotographyPostService;

@RestController
@RequestMapping("/api")
public class PhotographyPostController {

    private final PhotographyPostService photographyPostService;

    public PhotographyPostController(PhotographyPostService photographyPostService) {
        this.photographyPostService = photographyPostService;
    }

    @PostMapping("/posts")
    public ResponseEntity<PhotographyPost> createPost(@RequestBody PhotographyPost post) {
        return ResponseEntity.ok(photographyPostService.createPost(post));
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PhotographyPost>> getAllPosts() {
        return ResponseEntity.ok(photographyPostService.getAllPosts());
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PhotographyPost> getPostById(@PathVariable String id) {
        PhotographyPost post = photographyPostService.getPostById(id);
        return (post != null) ? ResponseEntity.ok(post) : ResponseEntity.notFound().build();
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<PhotographyPost> updatePost(@PathVariable String id, @RequestBody PhotographyPost post) {
        PhotographyPost existing = photographyPostService.getPostById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        post.setId(id);
        return ResponseEntity.ok(photographyPostService.updatePost(post));
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id) {
        photographyPostService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
