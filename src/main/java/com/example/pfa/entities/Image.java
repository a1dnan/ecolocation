package com.example.pfa.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imageId;

    private String type;

    @Column(length = 50000000)
    private byte[] imgByte;

    @ManyToOne
    @JsonIgnoreProperties("images")
    private Annonce annonce;


    public Image(String contentType, byte[] bytes) {
        this.type = contentType;
        this.imgByte = bytes;
    }
}
