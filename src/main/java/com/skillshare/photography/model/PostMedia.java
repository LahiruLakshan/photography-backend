package com.skillshare.photography.model;

import com.skillshare.photography.enums.MediaType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;



@Entity
@Table(name = "post_media")
@Data
public class PostMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private PhotographyPost post;
    
    @NotBlank
    private String mediaUrl;
    
    @Enumerated(EnumType.STRING)
    private MediaType mediaType;
    
    private int displayOrder;
}

