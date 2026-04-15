package org.example.beam.dto;

import java.util.List;

public class ShowPurchaseDto {
    private List<PurchaseListDto> gameLibrary;

    public List<PurchaseListDto> getGameLibrary() {
        return gameLibrary;
    }

    public void setGameLibrary(List<PurchaseListDto> gameLibrary) {
        this.gameLibrary = gameLibrary;
    }

}
