package org.example.beam.dto;

import java.math.BigDecimal;

public record ShowGameDto(
        Long id,
        String title,
        String genre,
        BigDecimal price,
        Long publisherId,
        String publisherName,
        String description,
        String releaseDate
) {}
