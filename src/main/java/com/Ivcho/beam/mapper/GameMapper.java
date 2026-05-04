package com.Ivcho.beam.mapper;

import com.Ivcho.beam.dto.ShowGameDto;
import com.Ivcho.beam.model.Game;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GameMapper {

    @Mapping(source = "publisher.name", target = "publisherName")
    ShowGameDto toDto(Game game);
}