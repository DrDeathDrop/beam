package com.Ivcho.beam.controller;

import com.Ivcho.beam.dto.CreatePurchaseDto;
import com.Ivcho.beam.dto.ShowPurchaseDto;
import com.Ivcho.beam.service.PurchaseService;
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
        if (createPurchaseDto.paymentMethod() == null) {
            return "Please provide a valid payment method";
        }
        purchaseService.buyGame(userId, id, createPurchaseDto.paymentMethod());
        return "Game purchased successfully";
    }

    @GetMapping("/show/{id}")
    public ShowPurchaseDto showPurchase(@PathVariable Long id) {

        return purchaseService.getPurchases(id);
    }
}