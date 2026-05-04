package org.example.beam.dto;

import java.math.BigDecimal;


public record UpdateGameDto(
         String title,
         String genre,
         BigDecimal price,
         String releaseDate,
         String description,
         Long publisherId
 ){}