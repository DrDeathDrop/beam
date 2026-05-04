package org.example.beam.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public record ShowGameDto(
        Long id,
        String title,
        String genre,
        BigDecimal price,
        String publisherName,
        String description,
        String releaseDate
) {}
