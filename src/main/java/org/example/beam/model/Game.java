package org.example.beam.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

   public void setTitle(String title) {
       this.title = title;
   }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

   public void setReleaseDate(String releaseDate) {
       this.releaseDate = releaseDate;
   }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
