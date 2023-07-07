package com.example.pfa.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Annonce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long annonceId;
    private String description;
    private LocalDate datePub;
    private double prix;
    @Enumerated(EnumType.STRING)
    private Duree duree;
    @Enumerated(EnumType.STRING)
    private Type type;
    
    @ManyToOne
    @JsonIgnoreProperties("annonce")
    private User user;

    private String ville;
    private String quartier;
    private LocalDate disponibilite;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnoreProperties("annonce")
    private List<Image> images;

    @ManyToMany(mappedBy = "annonceFavorites", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("annonceFavorites")
    private List<User> favorites;
    

}
