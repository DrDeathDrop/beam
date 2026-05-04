package com.Ivcho.beam.controller;

import com.Ivcho.beam.dto.CreatePurchaseDto;
import com.Ivcho.beam.dto.PurchaseListDto;
import com.Ivcho.beam.dto.ShowPurchaseDto;
import com.Ivcho.beam.service.PurchaseService;
import com.Ivcho.beam.enumeration.PaymentMethod;
import com.Ivcho.beam.enumeration.PurchaseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
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
        Long userId = 1L, gameId = 2L;
        CreatePurchaseDto dto = new CreatePurchaseDto(null, PaymentMethod.CREDIT_CARD, null, userId, gameId);

        String result = purchaseController.buyGame(userId, gameId, dto);

        assertEquals("Game purchased successfully", result);
        verify(purchaseService).buyGame(userId, gameId, PaymentMethod.CREDIT_CARD);
    }

    @Test
    void buyGame_missingPaymentMethod() {
        Long userId = 1L, gameId = 2L;
        CreatePurchaseDto dto = new CreatePurchaseDto(null, null, null, userId, gameId);

        String result = purchaseController.buyGame(userId, gameId, dto);

        assertEquals("Please provide a valid payment method", result);
        verify(purchaseService, never()).buyGame(any(), any(), any());
    }

    @Test
    void showPurchase_success() {
        Long userId = 1L;
        List<PurchaseListDto> library = List.of(
                new PurchaseListDto(BigDecimal.valueOf(29.99), PaymentMethod.G_PAY, PurchaseStatus.COMPLETED, "Some Game")
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