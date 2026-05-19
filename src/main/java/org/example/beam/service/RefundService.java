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
    public Purchase refundPurchase(String email, Long purchaseId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new RuntimeException("Couldn't find the specified purchase"));

        if (!purchase.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("This purchase does not belong to you");
        }
        if (purchase.getStatus() == PurchaseStatus.REFUNDED) {
            throw new RuntimeException("Purchase has already been refunded");
        }

        purchase.setStatus(PurchaseStatus.REFUNDED);
        return purchaseRepository.save(purchase);
    }
}


