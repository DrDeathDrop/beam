package com.Ivcho.beam.mapper;

import com.Ivcho.beam.dto.ShowUserDto;
import com.Ivcho.beam.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    ShowUserDto toDto(User user);
}
