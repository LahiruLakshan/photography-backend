package com.skillshare.photography.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.skillshare.photography.model.PhotographyPost;
import com.skillshare.photography.repository.PhotographyPostRepository;

@Service
public class PhotographyPostService {

    private final PhotographyPostRepository repository;

    public PhotographyPostService(PhotographyPostRepository repository) {
        this.repository = repository;
    }

    public PhotographyPost createPost(PhotographyPost post) {
        repository.save(post);
        return post;
    }

    public PhotographyPost getPostById(String id) {
        return repository.findById(id);
    }

    public List<PhotographyPost> getAllPosts() {
        return repository.findAll();
    }

    public void deletePost(String id) {
        repository.deleteById(id);
    }

    public PhotographyPost updatePost(PhotographyPost post) {
        repository.save(post);  // save handles both insert and update
        return post;
    }
}
