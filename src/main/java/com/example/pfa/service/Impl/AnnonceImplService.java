package com.example.pfa.service.Impl;

import com.example.pfa.entities.Image;
import com.example.pfa.entities.User;
import com.example.pfa.repos.AnnonceRepos;
import com.example.pfa.repos.ImageRepos;
import com.example.pfa.repos.UserRepos;
import com.example.pfa.service.AnnonceService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import com.example.pfa.entities.Annonce;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AnnonceImplService implements AnnonceService {

    private AnnonceRepos annonceRepos;
    private UserRepos userRepos;
    private ImageRepos imageRepos;


    public AnnonceImplService(AnnonceRepos annonceRepos, UserRepos userRepos, ImageRepos imageRepos) {
        this.annonceRepos = annonceRepos;
        this.userRepos = userRepos;
        this.imageRepos = imageRepos;
    }




    @Override
    public Annonce loadAnnonceById(Long anonceId) {
        return annonceRepos.findById(anonceId).orElseThrow(() -> new EntityNotFoundException("Anonce with ID " + anonceId + " Not Found"));
    }
    @Override
    public Annonce createAnnonce(Annonce annonce, Long userId) {
        User user=userRepos.findById(userId).orElseThrow(() -> new EntityNotFoundException("User with ID " + annonce.getUser().getUserId()+ " Not Found"));
        annonce.setUser(user);
        annonce.setDatePub(LocalDate.now());
        return annonceRepos.save(annonce);
    }

    @Override
    public Annonce createAnnonceImg(Annonce annonce, MultipartFile[] file, Long userId) throws IOException {

        List<Image> images = new ArrayList<>();
        for(MultipartFile img : file ) {
            Image image = new Image(img.getContentType(),img.getBytes());
            image.setAnnonce(annonce);
            images.add(image);
        }
        User user=userRepos.findById(userId).orElseThrow(() -> new EntityNotFoundException("User Not Found"));
        annonce.setUser(user);
        annonce.setDatePub(LocalDate.now());
        annonce.setImages(images);

        return annonceRepos.save(annonce);

    }


    @Override
    public Annonce updateAnnonce(Annonce annonce) {
        Annonce loadedCourse = loadAnnonceById(annonce.getAnnonceId());
        User user=userRepos.findById(annonce.getUser().getUserId()).orElseThrow(() -> new EntityNotFoundException("User with ID " + annonce.getUser().getUserId() + " Not Found"));
        annonce.setUser(user);
        return annonceRepos.save(annonce);
    }

    @Override
    public Page<Annonce> findAnnonceByVille(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Annonce> AnnoncePage= annonceRepos.findAnnoncesByVilleContains(keyword,pageRequest);
         return new PageImpl<>(AnnoncePage.getContent(), pageRequest, AnnoncePage.getTotalElements());
    }

    @Override
    public Page<Annonce> fetchAnnoncesForUser(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Annonce> annonceUserPage= annonceRepos.getAnnoncesByUserId(userId,pageRequest);
        return new PageImpl<>(annonceUserPage.getContent(), pageRequest, annonceUserPage.getTotalElements());
    }

    @Override
    public Page<Annonce> fetchAnnoncesFavorisForUser(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Annonce> userFavorisPage =annonceRepos.getAnnonceFavorisByUserId(userId,pageRequest);
        return new PageImpl<>(userFavorisPage.getContent().stream().collect(Collectors.toList()), pageRequest, userFavorisPage.getTotalElements());
    }

    @Override
    public List<Annonce> findAll() {
        return annonceRepos.findAll();
    }

    @Override
    public void removeAnnonce(Long annonceId) {
        Optional<Annonce> annonce = annonceRepos.findById(annonceId);

        if (annonce.isPresent()) {

            imageRepos.deleteImgAn(annonceId);
            imageRepos.deleteImage(annonceId);
            annonceRepos.deleteAnnonce(annonceId);
//            annonceRepos.delete(annonce.get());

        }
    }

    @Override
    public Annonce findAnnonceById(long id) {
        return annonceRepos.findById(id).orElseThrow(() -> new EntityNotFoundException("Annonce Not Found"));
    }
}
