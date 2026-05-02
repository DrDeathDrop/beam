package org.example.beam.service;

import org.example.beam.dto.PurchaseListDto;
import org.example.beam.dto.ShowPurchaseDto;
import org.example.beam.enumeration.*;
import org.example.beam.model.*;
import org.example.beam.repository.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PurchaseServiceTests {

    @InjectMocks
    private PurchaseService purchaseService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PurchaseRepository purchaseRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buyGame_success() {
        Long userId = 1L;
        Long gameId = 2L;

        User user = new User();
        user.setId(userId);

        Game game = new Game();
        game.setId(gameId);
        game.setPrice(BigDecimal.valueOf(59.99));

        PaymentMethod method = PaymentMethod.valueOf("CREDIT_CARD");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(purchaseRepository.save(any(Purchase.class))).thenAnswer(i -> i.getArguments()[0]);

        Purchase completedPurchase = purchaseService.buyGame(userId, gameId, method);

        assertNotNull(completedPurchase);
        assertEquals(user, completedPurchase.getUser());
        assertEquals(game, completedPurchase.getGame());
        assertEquals(BigDecimal.valueOf(59.99), completedPurchase.getPricePaid());
        assertEquals(method, completedPurchase.getPaymentMethod());
        assertEquals(PurchaseStatus.COMPLETED, completedPurchase.getStatus());

        verify(purchaseRepository, times(1)).save(any(Purchase.class));
    }

    @Test
    void buyGame_userNotFound_throwsException() {
        Long userId = 99L;
        Long gameId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> purchaseService.buyGame(userId, gameId, PaymentMethod.valueOf("CREDIT_CARD")));

        assertEquals("User not found", exception.getMessage());

        verify(gameRepository, never()).findById(anyLong());
        verify(purchaseRepository, never()).save(any());
    }

    @Test
    void buyGame_gameNotFound_throwsException() {
        Long userId = 1L;
        Long gameId = 88L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> purchaseService.buyGame(userId, gameId, PaymentMethod.valueOf("CREDIT_CARD")));

        assertEquals("Game not found", exception.getMessage());

        verify(userRepository, times(1)).findById(userId);
        verify(purchaseRepository, never()).save(any());
    }

    @Test
    void getPurchases_success() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Game game = new Game();
        game.setTitle("TestGame");

        Purchase purchase = new Purchase();
        purchase.setGame(game);
        purchase.setPricePaid(BigDecimal.valueOf(29.99));
        purchase.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        purchase.setStatus(PurchaseStatus.COMPLETED);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(purchaseRepository.findAllByUserId(userId)).thenReturn(List.of(purchase));

        ShowPurchaseDto result = purchaseService.getPurchases(userId);

        assertNotNull(result);
        assertNotNull(result.getGameLibrary());
        assertEquals(1, result.getGameLibrary().size());

        PurchaseListDto item = result.getGameLibrary().get(0);
        assertEquals("TestGame", item.getGameName());
        assertEquals(BigDecimal.valueOf(29.99), item.getPricePaid());
        assertEquals(PaymentMethod.CREDIT_CARD, item.getPaymentMethod());
        assertEquals(PurchaseStatus.COMPLETED, item.getStatus());

        verify(userRepository).findById(userId);
        verify(purchaseRepository).findAllByUserId(userId);
    }

    @Test
    void getPurchases_userNotFound_throwsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> purchaseService.getPurchases(99L));

        assertEquals("User not found", exception.getMessage());
        verify(purchaseRepository, never()).findAllByUserId(anyLong());
    }

}