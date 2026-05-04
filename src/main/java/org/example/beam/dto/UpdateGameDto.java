package org.example.beam.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class UpdateGameDto {
    private Long id;
    private String title;
    private String genre;
    private BigDecimal price;
    private String releaseDate;
    private String description;
    private Long publisherId;

}