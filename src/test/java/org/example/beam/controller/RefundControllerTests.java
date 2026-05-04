package org.example.beam.controller;

import org.example.beam.service.RefundService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RefundControllerTests {

    @InjectMocks
    private RefundController refundController;

    @Mock
    private RefundService refundService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void refundGame_success() {
        Long userId = 1L, purchaseId = 2L;

        String result = refundController.refundGame(userId, purchaseId);

        assertEquals("Game refunded successfully", result);
        verify(refundService).refundPurchase(userId, purchaseId);
    }

    @Test
    void refundGame_purchaseNotFound() {
        Long userId = 1L, purchaseId = 2L;

        doThrow(new RuntimeException("Couldn't find the specified purchase"))
                .when(refundService).refundPurchase(userId, purchaseId);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> refundController.refundGame(userId, purchaseId));

        assertEquals("Couldn't find the specified purchase", ex.getMessage());
    }

    @Test
    void refundGame_userNotFound() {
        Long userId = 1L, purchaseId = 2L;

        doThrow(new RuntimeException("User not found"))
                .when(refundService).refundPurchase(userId, purchaseId);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> refundController.refundGame(userId, purchaseId));

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void refundGame_alreadyRefunded() {
        Long userId = 1L, purchaseId = 2L;

        doThrow(new RuntimeException("Purchase has already been refunded"))
                .when(refundService).refundPurchase(userId, purchaseId);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> refundController.refundGame(userId, purchaseId));

        assertEquals("Purchase has already been refunded", ex.getMessage());
    }
}