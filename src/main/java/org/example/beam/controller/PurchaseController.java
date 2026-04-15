package org.example.beam.controller;

import org.example.beam.dto.CreatePurchaseDto;
import org.example.beam.dto.PurchaseListDto;
import org.example.beam.dto.ShowPurchaseDto;
import org.example.beam.model.Game;
import org.example.beam.model.Purchase;
import org.example.beam.model.User;
import org.example.beam.repository.GameRepository;
import org.example.beam.repository.PurchaseRepository;
import org.example.beam.repository.UserRepository;
import org.example.beam.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/buy/{userId}/{id}")
    public String buyGame(@PathVariable Long userId, @PathVariable Long id, @RequestBody CreatePurchaseDto createPurchaseDto){

        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(createPurchaseDto.getPaymentMethod() == null){
            return "Please provide a valid payment method";
        }
        purchaseService.buyGame(userId, id, createPurchaseDto.getPaymentMethod());

        return "Game purchased successfully";
    }

    @GetMapping("/show/{id}")
    public ShowPurchaseDto showPurchase(@PathVariable Long id){

        userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Purchase> purchases = purchaseRepository.findAllByUserId(id);

        List<PurchaseListDto> purchaseDto = purchases.stream()
                .map(p -> {
                    PurchaseListDto dto = new PurchaseListDto();
                    dto.setGameName(p.getGame().getTitle());
                    dto.setPricePaid(p.getPricePaid());
                    dto.setPaymentMethod(p.getPaymentMethod());
                    dto.setStatus(p.getStatus());
                    return dto;
                })
                .toList();

        ShowPurchaseDto dto = new ShowPurchaseDto();
        dto.setGameLibrary(purchaseDto);

        return dto;
    }

}