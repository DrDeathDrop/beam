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

        Game game = new Game();
        User user = new User();

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        String result = purchaseController.buyGame(userId, gameId, dto);

        assertEquals("Game purchased successfully", result);
        verify(purchaseService).buyGame(userId, gameId, PaymentMethod.CREDIT_CARD);
    }

    @Test
    void buyGame_missingPaymentMethod() {
        Long userId = 1L, gameId = 2L;
        CreatePurchaseDto dto = new CreatePurchaseDto();
        dto.setPaymentMethod(null);

        Game game = new Game();
        User user = new User();

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        String result = purchaseController.buyGame(userId, gameId, dto);

        assertEquals("Please provide a valid payment method", result);
        verify(purchaseService, never()).buyGame(any(), any(), any());
    }

    @Test
    void buyGame_userNotFound() {
        Long userId = 1L, gameId = 2L;
        CreatePurchaseDto dto = new CreatePurchaseDto();
        dto.setPaymentMethod(PaymentMethod.G_PAY);
        Game game = new Game();

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () -> purchaseController.buyGame(userId, gameId, dto));
        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void buyGame_gameNotFound() {
        Long userId = 1L, gameId = 2L;
        CreatePurchaseDto dto = new CreatePurchaseDto();
        dto.setPaymentMethod(PaymentMethod.DEBIT_CARD);

        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () -> purchaseController.buyGame(userId, gameId, dto));
        assertEquals("Game not found", ex.getMessage());
    }

    @Test
    void showPurchase_success() {
        Long userId = 5L;
        User user = new User();
        Purchase purchase = new Purchase();
        Game game = new Game();
        game.setTitle("MyGame");
        purchase.setGame(game);
        purchase.setPricePaid(new BigDecimal("29.99"));
        purchase.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        purchase.setStatus(PurchaseStatus.COMPLETED);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(purchaseRepository.findAllByUserId(userId)).thenReturn(List.of(purchase));

        ShowPurchaseDto result = purchaseController.showPurchase(userId);

        assertNotNull(result.getGameLibrary());
        assertEquals(1, result.getGameLibrary().size());
        PurchaseListDto dto = result.getGameLibrary().get(0);
        assertEquals("MyGame", dto.getGameName());
        assertEquals(new BigDecimal("29.99"), dto.getPricePaid());
        assertEquals(PaymentMethod.CREDIT_CARD, dto.getPaymentMethod());
        assertEquals(PurchaseStatus.COMPLETED, dto.getStatus());
    }

    @Test
    void showPurchase_userNotFound() {
        Long userId = 10L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception e = assertThrows(RuntimeException.class, () -> purchaseController.showPurchase(userId));
        assertEquals("User not found", e.getMessage());
    }

    @Test
    void showPurchase_noPurchases() {
        Long userId = 8L;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(purchaseRepository.findAllByUserId(userId)).thenReturn(List.of());

        ShowPurchaseDto result = purchaseController.showPurchase(userId);
        assertNotNull(result.getGameLibrary());
        assertTrue(result.getGameLibrary().isEmpty());
    }
}