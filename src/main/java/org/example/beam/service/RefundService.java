package org.example.beam.service;

import org.example.beam.model.Purchase;
import org.example.beam.model.User;
import org.example.beam.repository.PurchaseRepository;
import org.example.beam.repository.UserRepository;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.example.beam.enumeration.PurchaseStatus;

@Service
public class RefundService {

    private final UserRepository userRepository;
    private final PurchaseRepository purchaseRepository;

    public RefundService(UserRepository userRepository, PurchaseRepository purchaseRepository) {
        this.userRepository = userRepository;
        this.purchaseRepository = purchaseRepository;
    }

    @Transactional
    public Purchase refundPurchase(Long userId, Long purchaseId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new RuntimeException("Couldn't find the specified purchase"));

        if (!purchase.getUser().getId().equals(userId)) {
            throw new RuntimeException("You do not have this product, so no refunds :)");
        }

        if (purchase.getStatus() == PurchaseStatus.REFUNDED) {
            throw new RuntimeException("Purchase has already been refunded");
        }

        purchase.setStatus(PurchaseStatus.REFUNDED);
        Purchase refundedPurchase = purchaseRepository.save(purchase);

        return refundedPurchase;

    }
}


