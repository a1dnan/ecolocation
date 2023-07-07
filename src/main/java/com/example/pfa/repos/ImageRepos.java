package com.example.pfa.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pfa.entities.Image;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ImageRepos extends JpaRepository<Image, Long> {

    @Modifying
    @Query(value = "DELETE FROM image WHERE annonce_annonce_id=:annonceId", nativeQuery = true)
    void deleteImage(long annonceId);
    @Modifying
    @Query(value = "DELETE FROM annonce_images WHERE annonce_annonce_id=:annonceId", nativeQuery = true)
    void deleteImgAn(long annonceId);
}
