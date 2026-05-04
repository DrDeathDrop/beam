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
import org.example.beam.enumeration.PaymentMethod;
import org.example.beam.enumeration.PurchaseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PurchaseControllerTests {

    @InjectMocks
    private PurchaseController purchaseController;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private PurchaseService purchaseService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buyGame_success() {

    }

    @Test
    void buyGame_missingPaymentMethod() {

    }

    @Test
    void showPurchase_success() {

    }

    @Test
    void showPurchase_userNotFound() {
        when(purchaseService.getPurchases(10L)).thenThrow(new RuntimeException("User not found"));

        assertThrows(RuntimeException.class, () -> purchaseController.showPurchase(10L));
    }

    @Test
    void showPurchase_noPurchases() {

    }



}