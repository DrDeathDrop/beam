package org.example.beam.controller;

import org.example.beam.dto.CreatePurchaseDto;
import org.example.beam.dto.PurchaseListDto;
import org.example.beam.dto.ShowPurchaseDto;
import org.example.beam.service.PurchaseService;
import org.example.beam.enumeration.PaymentMethod;
import org.example.beam.enumeration.PurchaseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PurchaseControllerTests {

    @InjectMocks
    private PurchaseController purchaseController;

    @Mock
    private PurchaseService purchaseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buyGame_success() {
        Long gameId = 2L;
        String email = "buyer@email.com";
        CreatePurchaseDto dto = new CreatePurchaseDto(null, PaymentMethod.CREDIT_CARD, null, null, gameId);

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(email);

        ResponseEntity<String> result = purchaseController.buyGame(gameId, dto, principal);

        assertEquals("Game purchased successfully", result.getBody());
        verify(purchaseService).buyGame(email, gameId, PaymentMethod.CREDIT_CARD);
    }

    @Test
    void buyGame_missingPaymentMethod() {
        Long gameId = 2L;
        CreatePurchaseDto dto = new CreatePurchaseDto(null, null, null, null, gameId);

        Principal principal = mock(Principal.class);

        ResponseEntity<String> result = purchaseController.buyGame(gameId, dto, principal);

        assertEquals("Please provide a valid payment method", result.getBody());
        verify(purchaseService, never()).buyGame(any(), any(), any());
    }

    @Test
    void showPurchase_success() {
        Long userId = 1L;
        List<PurchaseListDto> library = List.of(
                new PurchaseListDto(1L, BigDecimal.valueOf(29.99), PaymentMethod.G_PAY, PurchaseStatus.COMPLETED, "Some Game")
        );
        ShowPurchaseDto dto = new ShowPurchaseDto(library);

        when(purchaseService.getPurchases(userId)).thenReturn(dto);

        ShowPurchaseDto result = purchaseController.showPurchase(userId);

        assertNotNull(result);
        assertEquals(1, result.gameLibrary().size());
        assertEquals("Some Game", result.gameLibrary().get(0).gameName());
    }

    @Test
    void showPurchase_userNotFound() {
        when(purchaseService.getPurchases(10L)).thenThrow(new RuntimeException("User not found"));

        assertThrows(RuntimeException.class, () -> purchaseController.showPurchase(10L));
    }

    @Test
    void showPurchase_noPurchases() {
        Long userId = 1L;
        ShowPurchaseDto dto = new ShowPurchaseDto(List.of());

        when(purchaseService.getPurchases(userId)).thenReturn(dto);

        ShowPurchaseDto result = purchaseController.showPurchase(userId);

        assertNotNull(result);
        assertTrue(result.gameLibrary().isEmpty());
    }
}
