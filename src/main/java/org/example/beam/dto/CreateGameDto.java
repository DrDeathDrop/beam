package org.example.beam.dto;

import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;

public record CreateGameDto (

     String title,
     String genre,
     BigDecimal price,
     Long publisherId,
     String description,
     String releaseDate
    )

{}