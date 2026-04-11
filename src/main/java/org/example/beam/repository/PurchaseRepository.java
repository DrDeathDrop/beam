package org.example.beam.repository;

import org.example.beam.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    Optional<Purchase> findById(Long id);
    Optional<Purchase> findByUser(Long userId);
    Optional<Purchase> findByGame(Long gameId);
    Optional<Purchase> findByStatus(String status);
    Optional<Purchase> findByPricePaid(BigDecimal pricePaid);
    Optional<Purchase> findByPaymentMethod(String paymentMethod);


}
