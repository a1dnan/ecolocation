package com.example.pfa.web;

import com.example.pfa.entities.Annonce;
import com.example.pfa.service.AnnonceService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/annonces")
@CrossOrigin("*")
public class AnnonceRest {
    private final AnnonceService annonceService;

    public AnnonceRest(AnnonceService annonceService) {
        this.annonceService = annonceService;
    }

    @GetMapping("/search")
    public Page<Annonce> searchAnnonces(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                        @RequestParam(name = "size", defaultValue = "5") int size) {
        return annonceService.findAnnonceByVille(keyword, page, size);

    }

    @GetMapping
    public List<Annonce> findAllAnnonces(){
        return annonceService.findAll();
    }

    @GetMapping("/{id}")
    public Annonce findAnnonce(@PathVariable Long id){

        return annonceService.findAnnonceById(id);
    }

    @DeleteMapping("/{annonceId}")
    public void deleteAnnonce(@PathVariable Long annonceId) {
       annonceService.removeAnnonce(annonceId);
    }

    @PostMapping("/{userId}")
    public Annonce saveAnnonce(@RequestBody Annonce annonce, @PathVariable Long userId) {
        return annonceService.createAnnonce(annonce, userId);
    }

    @PostMapping(value = {"/add/{userId}"},consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Annonce addNewAnnonce(@RequestPart("annonce") Annonce annonce,
                                 @PathVariable("userId") Long userId,
                                 @RequestPart("imageFile") MultipartFile[] file ) throws IOException {
        return annonceService.createAnnonceImg(annonce, file, userId);
    }




}
