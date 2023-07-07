package com.example.pfa.web;

import com.example.pfa.entities.Annonce;
import com.example.pfa.entities.User;
import com.example.pfa.service.AnnonceService;
import com.example.pfa.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Users")
@CrossOrigin("*")
public class UserRest {

   UserService userService;
   AnnonceService annonceService;

    public UserRest(UserService userService, AnnonceService annonceService) {
        this.userService = userService;
        this.annonceService = annonceService;
    }

    @GetMapping
    public Page<User> searchUsers(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                     @RequestParam(name = "page", defaultValue = "0") int page,
                                     @RequestParam(name = "size", defaultValue = "5") int size) {
        return userService.loadUsersByName(keyword, page, size);
    }

    @GetMapping("{userId}")
    public User getUser(@PathVariable long userId) {
        return userService.loadUserById(userId);
    }

    @DeleteMapping("/{anonceId}")
    public void deleteUser(@PathVariable Long anonceId) {
        userService.removeUser(anonceId);
    }

    @PostMapping
    public User saveUser(@RequestBody User user) {
       return userService.createUser(user);
    }

    @PutMapping("/{userId}")
    public User updateUser(@RequestBody User user, @PathVariable Long userId) {
        user.setUserId((userId));
        return userService.updateUser(user);
    }
    @GetMapping("/{userId}/annonces")
    public Page<Annonce> annoncesByUserId(@PathVariable Long userId,
                                          @RequestParam(name = "page", defaultValue = "0") int page,
                                          @RequestParam(name = "size", defaultValue = "5") int size) {
        return annonceService.fetchAnnoncesForUser(userId,page,size);
    }

    @GetMapping("/favoris/{userId}")
    public Page<Annonce> favorisByUserId(@PathVariable Long userId,
                                         @RequestParam(name = "page", defaultValue = "0") int page,
                                         @RequestParam(name = "size", defaultValue = "5") int size) {

        return annonceService.fetchAnnoncesFavorisForUser(userId,page,size);
    }

    @PostMapping("/fav/{userId}/{annonceId}")
    public void saveFavorie(@PathVariable Long userId,@PathVariable Long annonceId) {
        userService.seveFavorie(userId,annonceId);
    }
    @DeleteMapping("/fav/{userId}/{annonceId}")
    public void deleteFavorie(@PathVariable Long userId,@PathVariable Long annonceId) {
        userService.deleteFavorie(userId,annonceId);
    }

}
