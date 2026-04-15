package org.example.beam.controller;

import org.example.beam.model.Game;
import org.example.beam.model.Purchase;
import org.example.beam.model.User;
import org.example.beam.repository.GameRepository;
import org.example.beam.repository.PurchaseRepository;
import org.example.beam.repository.UserRepository;
import org.example.beam.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/refunds")
public class RefundController {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefundService refundService;
    @Autowired
    private PurchaseRepository purchaseRepository;

    @PostMapping("/{userId}/{id}")
    public String refundGame(@PathVariable Long userId, @PathVariable Long id){

        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("refund not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

         refundService.refundPurchase(userId, id);

         return "Game refunded successfully";

    }

}
