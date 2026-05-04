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
    RefundService refundService;

    @PostMapping("/{userId}/{id}")
    public String refundGame(@PathVariable Long userId, @PathVariable Long id) {
        refundService.refundPurchase(userId, id);
        return "Game refunded successfully";
    }

}
