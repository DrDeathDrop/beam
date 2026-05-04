package com.Ivcho.beam.mapper;

import com.Ivcho.beam.dto.ShowPublisherDto;
import com.Ivcho.beam.model.Publisher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PublisherMapper {

    ShowPublisherDto toDto(Publisher publisher);
}