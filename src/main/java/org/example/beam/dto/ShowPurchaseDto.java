package org.example.beam.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ShowPurchaseDto {
    private List<PurchaseListDto> gameLibrary;

}
