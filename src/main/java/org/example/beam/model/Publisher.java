package org.example.beam.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "publishers")
public class Publisher {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String country;
    private String yearsOfEstablishment;
    private String founded;
    private String website;

    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL)
    private List<Game> games = new ArrayList<>();


    public Publisher(String name, String country, String founded, String website, String yearsOfEstablishment) {
        this.name = name;
        this.country = country;
        this.yearsOfEstablishment = yearsOfEstablishment;
        this.founded = founded;
        this.website = website;
    }

    public Publisher() {

    }
}
