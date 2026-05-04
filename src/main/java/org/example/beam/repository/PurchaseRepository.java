package org.example.beam.repository;

import org.example.beam.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    Optional<Purchase> findById(Long id);
    List<Purchase> findAllByUserId(Long userId);



}
