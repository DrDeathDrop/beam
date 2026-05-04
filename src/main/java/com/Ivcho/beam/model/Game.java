package com.Ivcho.beam.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    private String releaseDate;

    private String genre;

    private BigDecimal price;

    private String description;

    public Game( String title, Publisher publisher, String releaseDate, String genre, BigDecimal price, String description) {
        this.title = title;
        this.publisher = publisher;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.price = price;
        this.description = description;
    }

    public Game() {

    }

}
