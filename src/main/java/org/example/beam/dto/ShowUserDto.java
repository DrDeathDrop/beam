package org.example.beam.dto;

import java.util.List;

public record ShowUserDto (
        Long id,
        String name,
        String email,
        List<PurchaseListDto> ownedGames

){}
