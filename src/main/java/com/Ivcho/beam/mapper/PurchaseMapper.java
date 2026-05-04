package com.Ivcho.beam.mapper;

import com.Ivcho.beam.dto.PurchaseListDto;
import com.Ivcho.beam.model.Purchase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {

    @Mapping(source = "game.title", target = "gameName")
    PurchaseListDto toListDto(Purchase purchase);
}