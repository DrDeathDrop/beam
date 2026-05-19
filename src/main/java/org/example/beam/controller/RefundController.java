package org.example.beam.controller;


import org.example.beam.service.RefundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/refunds")
public class RefundController {

    final
    RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    @PostMapping("/{purchaseId}")
    public ResponseEntity<String> refundGame(@PathVariable Long purchaseId, Principal principal) {
        refundService.refundPurchase(principal.getName(), purchaseId);
        return ResponseEntity.ok("Game refunded successfully");
    }

}
