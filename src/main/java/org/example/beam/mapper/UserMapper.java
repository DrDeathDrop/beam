package org.example.beam.mapper;

import org.example.beam.dto.ShowUserDto;
import org.example.beam.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PurchaseMapper.class)
public interface UserMapper {
    @Mapping(source = "purchases", target = "ownedGames")
    ShowUserDto toDto(User user);
}
