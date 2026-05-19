package org.example.beam.controller;

import org.example.beam.dto.CreatePurchaseDto;
import org.example.beam.dto.ShowPurchaseDto;
import org.example.beam.service.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping("/buy/{gameId}")
    public ResponseEntity<String> buyGame(
            @PathVariable Long gameId,
            @RequestBody CreatePurchaseDto createPurchaseDto,
            Principal principal) {

        if (createPurchaseDto.paymentMethod() == null) {
            return ResponseEntity.badRequest().body("Please provide a valid payment method");
        }
        purchaseService.buyGame(principal.getName(), gameId, createPurchaseDto.paymentMethod());
        return ResponseEntity.ok("Game purchased successfully");
    }
    @GetMapping("/show/{id}")
    public ShowPurchaseDto showPurchase(@PathVariable Long id) {

        return purchaseService.getPurchases(id);
    }
}