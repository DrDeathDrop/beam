package org.example.beam.service;

import jakarta.transaction.Transactional;
import org.example.beam.dto.PurchaseListDto;
import org.example.beam.dto.ShowPurchaseDto;
import org.example.beam.enumeration.PaymentMethod;
import org.example.beam.repository.*;
import org.example.beam.model.*;
import org.springframework.stereotype.Service;
import org.example.beam.enumeration.PurchaseStatus;

import java.util.List;

@Service
public class PurchaseService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final PurchaseRepository purchaseRepository;

    public PurchaseService(UserRepository userRepository,
                           GameRepository gameRepository,
                           PurchaseRepository purchaseRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.purchaseRepository = purchaseRepository;
    }

    @Transactional
    public ShowPurchaseDto getPurchases(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<PurchaseListDto> purchaseDto = purchaseRepository.findAllByUserId(userId)
                .stream()
                .map(p -> new PurchaseListDto(
                        p.getPricePaid(),
                        p.getPaymentMethod(),
                        p.getStatus(),
                        p.getGame().getTitle()
                ))
                .toList();

        return new ShowPurchaseDto(purchaseDto);
    }

    @Transactional
    public Purchase buyGame(Long userId, Long gameId, PaymentMethod paymentMethod) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setGame(game);
        purchase.setPricePaid(game.getPrice());
        purchase.setPaymentMethod(paymentMethod);
        purchase.setStatus(PurchaseStatus.COMPLETED);

        return purchaseRepository.save(purchase);
    }
}