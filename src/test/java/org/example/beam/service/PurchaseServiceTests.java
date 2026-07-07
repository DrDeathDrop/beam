package org.example.beam.service;

import org.example.beam.dto.PurchaseListDto;
import org.example.beam.dto.ShowPurchaseDto;
import org.example.beam.enumeration.PaymentMethod;
import org.example.beam.enumeration.PurchaseStatus;
import org.example.beam.model.Game;
import org.example.beam.model.Purchase;
import org.example.beam.model.User;
import org.example.beam.repository.GameRepository;
import org.example.beam.repository.PurchaseRepository;
import org.example.beam.repository.UserRepository;
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
        String email = "buyer@email.com";
        Long gameId = 2L;

        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        Game game = new Game();
        game.setId(gameId);
        game.setPrice(BigDecimal.valueOf(59.99));

        PaymentMethod method = PaymentMethod.valueOf("CREDIT_CARD");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(purchaseRepository.save(any(Purchase.class))).thenAnswer(i -> i.getArguments()[0]);

        Purchase completedPurchase = purchaseService.buyGame(email, gameId, method);

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
        String email = "missing@email.com";
        Long gameId = 2L;

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> purchaseService.buyGame(email, gameId, PaymentMethod.valueOf("CREDIT_CARD")));

        assertEquals("User not found", exception.getMessage());

        verify(gameRepository, never()).findById(anyLong());
        verify(purchaseRepository, never()).save(any());
    }

    @Test
    void buyGame_gameNotFound_throwsException() {
        String email = "buyer@email.com";
        Long gameId = 88L;
        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> purchaseService.buyGame(email, gameId, PaymentMethod.valueOf("CREDIT_CARD")));

        assertEquals("Game not found", exception.getMessage());

        verify(userRepository, times(1)).findByEmail(email);
        verify(purchaseRepository, never()).save(any());
    }

    @Test
    void getPurchases_success() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Game game = new Game();
        game.setTitle("Test Game");
        game.setPrice(BigDecimal.valueOf(29.99));

        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setGame(game);
        purchase.setPricePaid(BigDecimal.valueOf(29.99));
        purchase.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        purchase.setStatus(PurchaseStatus.COMPLETED);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(purchaseRepository.findAllByUserId(userId)).thenReturn(List.of(purchase));

        ShowPurchaseDto result = purchaseService.getPurchases(userId);

        assertNotNull(result);
        assertEquals(1, result.gameLibrary().size());
        PurchaseListDto item = result.gameLibrary().get(0);
        assertEquals("Test Game", item.gameName());
        assertEquals(BigDecimal.valueOf(29.99), item.pricePaid());
        assertEquals(PaymentMethod.CREDIT_CARD, item.paymentMethod());
        assertEquals(PurchaseStatus.COMPLETED, item.status());
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