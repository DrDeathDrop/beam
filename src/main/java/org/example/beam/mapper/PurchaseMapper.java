package org.example.beam.mapper;

import org.example.beam.dto.PurchaseListDto;
import org.example.beam.model.Purchase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {

    @Mapping(source = "game.title", target = "gameName")
    PurchaseListDto toListDto(Purchase purchase);
}