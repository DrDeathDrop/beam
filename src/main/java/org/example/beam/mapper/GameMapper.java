package org.example.beam.mapper;

import org.example.beam.dto.ShowGameDto;
import org.example.beam.model.Game;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GameMapper {

    @Mapping(source = "publisher.name", target = "publisherName")
    ShowGameDto toDto(Game game);
}