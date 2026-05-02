package org.example.beam.mapper;

import org.example.beam.dto.ShowUserDto;
import org.example.beam.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    ShowUserDto toDto(User user);
}
