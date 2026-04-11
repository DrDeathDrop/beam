package org.example.beam.dto;

import java.math.BigDecimal;

public class UpdateGameDto {
    private Long id;
    private String title;
    private String genre;
    private BigDecimal price;
    private String releaseDate;
    private String description;
    private Long publisherId;

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

    public String getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPublisherId() {
        return publisherId;
    }
    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }
}