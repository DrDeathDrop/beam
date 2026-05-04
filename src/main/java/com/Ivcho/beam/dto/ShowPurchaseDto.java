package com.Ivcho.beam.dto;

import java.util.List;


public record ShowPurchaseDto (
         List<PurchaseListDto> gameLibrary
){}
