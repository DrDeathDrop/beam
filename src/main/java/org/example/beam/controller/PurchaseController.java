package org.example.beam.controller;

import org.example.beam.dto.CreatePurchaseDto;
import org.example.beam.dto.ShowPurchaseDto;
import org.example.beam.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("/buy/{userId}/{id}")
    public String buyGame(@PathVariable Long userId, @PathVariable Long id,
                          @RequestBody CreatePurchaseDto createPurchaseDto) {
        if (createPurchaseDto.getPaymentMethod() == null) {
            return "Please provide a valid payment method";
        }
        purchaseService.buyGame(userId, id, createPurchaseDto.getPaymentMethod());
        return "Game purchased successfully";
    }

    @GetMapping("/show/{id}")
    public ShowPurchaseDto showPurchase(@PathVariable Long id) {
        return purchaseService.getPurchases(id);
    }
}