package org.example.beam.controller;

import org.example.beam.service.RefundService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

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
        Long purchaseId = 2L;
        String email = "user@email.com";

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(email);

        ResponseEntity<String> result = refundController.refundGame(purchaseId, principal);

        assertEquals("Game refunded successfully", result.getBody());
        verify(refundService).refundPurchase(email, purchaseId);
    }

    @Test
    void refundGame_purchaseNotFound() {
        Long purchaseId = 2L;
        String email = "user@email.com";

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(email);

        doThrow(new RuntimeException("Couldn't find the specified purchase"))
                .when(refundService).refundPurchase(email, purchaseId);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> refundController.refundGame(purchaseId, principal));

        assertEquals("Couldn't find the specified purchase", ex.getMessage());
    }

    @Test
    void refundGame_userNotFound() {
        Long purchaseId = 2L;
        String email = "user@email.com";

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(email);

        doThrow(new RuntimeException("User not found"))
                .when(refundService).refundPurchase(email, purchaseId);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> refundController.refundGame(purchaseId, principal));

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void refundGame_alreadyRefunded() {
        Long purchaseId = 2L;
        String email = "user@email.com";

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(email);

        doThrow(new RuntimeException("Purchase has already been refunded"))
                .when(refundService).refundPurchase(email, purchaseId);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> refundController.refundGame(purchaseId, principal));

        assertEquals("Purchase has already been refunded", ex.getMessage());
    }
}
