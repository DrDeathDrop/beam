package org.example.beam.mapper;

import org.example.beam.dto.ShowPublisherDto;
import org.example.beam.model.Publisher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PublisherMapper {

    ShowPublisherDto toDto(Publisher publisher);
}