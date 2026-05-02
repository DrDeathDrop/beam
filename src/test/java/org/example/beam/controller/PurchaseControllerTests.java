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
        Long userId = 1L, gameId = 2L;
        CreatePurchaseDto dto = new CreatePurchaseDto();
        dto.setPaymentMethod(PaymentMethod.CREDIT_CARD);

        String result = purchaseController.buyGame(userId, gameId, dto);

        assertEquals("Game purchased successfully", result);
        verify(purchaseService).buyGame(userId, gameId, PaymentMethod.CREDIT_CARD);
    }

    @Test
    void buyGame_missingPaymentMethod() {
        Long userId = 1L, gameId = 2L;
        CreatePurchaseDto dto = new CreatePurchaseDto();
        dto.setPaymentMethod(null);

        String result = purchaseController.buyGame(userId, gameId, dto);

        assertEquals("Please provide a valid payment method", result);
        verify(purchaseService, never()).buyGame(any(), any(), any());
    }

    @Test
    void showPurchase_success() {
        Long userId = 5L;
        PurchaseListDto item = new PurchaseListDto();
        item.setGameName("MyGame");
        item.setPricePaid(new BigDecimal("29.99"));
        item.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        item.setStatus(PurchaseStatus.COMPLETED);

        ShowPurchaseDto showDto = new ShowPurchaseDto();
        showDto.setGameLibrary(List.of(item));

        when(purchaseService.getPurchases(userId)).thenReturn(showDto);

        ShowPurchaseDto result = purchaseController.showPurchase(userId);

        assertNotNull(result.getGameLibrary());
        assertEquals(1, result.getGameLibrary().size());
        assertEquals("MyGame", result.getGameLibrary().get(0).getGameName());
    }

    @Test
    void showPurchase_userNotFound() {
        when(purchaseService.getPurchases(10L)).thenThrow(new RuntimeException("User not found"));

        assertThrows(RuntimeException.class, () -> purchaseController.showPurchase(10L));
    }

    @Test
    void showPurchase_noPurchases() {
        ShowPurchaseDto empty = new ShowPurchaseDto();
        empty.setGameLibrary(List.of());

        when(purchaseService.getPurchases(8L)).thenReturn(empty);

        ShowPurchaseDto result = purchaseController.showPurchase(8L);
        assertNotNull(result.getGameLibrary());
        assertTrue(result.getGameLibrary().isEmpty());
    }



}