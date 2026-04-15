package org.example.beam.controller;

import org.example.beam.model.*;
import org.example.beam.repository.*;
import org.example.beam.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/refunds")
public class RefundController {

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
