package com.skillshare.photography.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillshare.photography.model.PhotographyPost;
import com.skillshare.photography.model.User;

public interface PhotographyPostRepository extends JpaRepository<PhotographyPost, Long> {
    List<PhotographyPost> findByUser(User user);
}
