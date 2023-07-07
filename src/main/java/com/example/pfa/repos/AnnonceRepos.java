package com.example.pfa.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pfa.entities.Annonce;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AnnonceRepos extends JpaRepository<Annonce, Long> {

    Page<Annonce> findAnnoncesByVilleContains(String keyword, Pageable pageable);

    @Query(value = "select c from Annonce as c where c.user.userId=:userId")
    Page<Annonce> getAnnoncesByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "select * from Annonce where annonce_id in (select e.annonce_id from annonces_favorites as e where e.user_id=:studentId)", nativeQuery = true)
    Page<Annonce> getAnnonceFavorisByUserId(@Param("studentId") Long studentId, Pageable pageable);

    Page<Annonce> findAllBy(Long studentId, Pageable pageable);

    Optional<Annonce> findByAnnonceId(long annonceId);

    @Modifying
    @Query(value = "DELETE FROM annonce WHERE annonce_id=:annonceId", nativeQuery = true)
    void deleteAnnonce(long annonceId);

}
