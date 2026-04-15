package org.example.beam.controller;

import org.example.beam.model.Purchase;
import org.example.beam.model.User;
import org.example.beam.repository.PurchaseRepository;
import org.example.beam.repository.UserRepository;
import org.example.beam.service.RefundService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RefundControllerTests {

    @InjectMocks
    private RefundController refundController;

    @Mock
    private PurchaseRepository purchaseRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RefundService refundService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void refundGame_success() {
        Long userId = 1L, purchaseId = 2L;
        Purchase purchase = new Purchase();
        User user = new User();
        
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchase));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        String result = refundController.refundGame(userId, purchaseId);

        assertEquals("Game refunded successfully", result);
        verify(refundService).refundPurchase(userId, purchaseId);
    }

    @Test
    void refundGame_purchaseNotFound() {
        Long userId = 1L, purchaseId = 2L;

        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () -> refundController.refundGame(userId, purchaseId));
        assertEquals("refund not found", ex.getMessage());
        verify(refundService, never()).refundPurchase(any(), any());
    }

    @Test
    void refundGame_userNotFound() {
        Long userId = 1L, purchaseId = 2L;
        Purchase purchase = new Purchase();

        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchase));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () -> refundController.refundGame(userId, purchaseId));
        assertEquals("User not found", ex.getMessage());
        verify(refundService, never()).refundPurchase(any(), any());
    }
}