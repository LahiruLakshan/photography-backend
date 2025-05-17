package com.skillshare.photography.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillshare.photography.model.PostMedia;

public interface PostMediaRepository extends JpaRepository<PostMedia, Long> {
    List<PostMedia> findByPostId(Long postId);
}