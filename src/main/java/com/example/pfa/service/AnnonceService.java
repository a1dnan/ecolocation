package com.example.pfa.service;

import com.example.pfa.entities.Annonce;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AnnonceService {

    Annonce loadAnnonceById(Long annonceId);

    Annonce createAnnonce(Annonce annonce, Long userId);

    Annonce createAnnonceImg(Annonce annonce, MultipartFile[] file, Long userId) throws IOException;

    Annonce updateAnnonce(Annonce annonce);

    Page<Annonce> findAnnonceByVille(String keyword, int page, int size);


    Page<Annonce> fetchAnnoncesForUser(Long userId, int page, int size);

    Page<Annonce> fetchAnnoncesFavorisForUser(Long userId, int page, int size);

    List<Annonce> findAll();

    void removeAnnonce(Long annonceId);


    Annonce findAnnonceById(long id);
}
