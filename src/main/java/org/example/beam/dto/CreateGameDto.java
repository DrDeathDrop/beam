package org.example.beam.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Setter
@Getter
public class CreateGameDto {
    private String title;
    private String genre;
    private BigDecimal price;
    private Long publisherId;
    private String description;
    private String releaseDate;

}