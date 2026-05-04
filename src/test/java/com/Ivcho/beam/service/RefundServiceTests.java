package com.Ivcho.beam.service;

import com.Ivcho.beam.enumeration.PurchaseStatus;
import com.Ivcho.beam.model.Purchase;
import com.Ivcho.beam.model.User;
import com.Ivcho.beam.repository.PurchaseRepository;
import com.Ivcho.beam.repository.UserRepository;
import org.example.beam.model.*;
import org.example.beam.repository.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RefundServiceTests {

    @InjectMocks
    private RefundService refundService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PurchaseRepository purchaseRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void refundPurchase_success() {
        Long userId = 1L;
        Long purchaseId = 100L;

        User user = new User();
        user.setId(userId);

        Purchase purchase = new Purchase();
        purchase.setId(purchaseId);
        purchase.setUser(user);
        purchase.setStatus(PurchaseStatus.COMPLETED);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchase));
        when(purchaseRepository.save(any(Purchase.class))).thenAnswer(i -> i.getArguments()[0]);

        Purchase refundedPurchase = refundService.refundPurchase(userId, purchaseId);

        assertNotNull(refundedPurchase);
        assertEquals(PurchaseStatus.REFUNDED, refundedPurchase.getStatus());
        verify(purchaseRepository, times(1)).save(purchase);
    }

    @Test
    void refundPurchase_userNotFound_throwsException() {
        Long userId = 99L;
        Long purchaseId = 100L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> refundService.refundPurchase(userId, purchaseId));

        assertEquals("User not found", exception.getMessage());
        verify(purchaseRepository, never()).findById(anyLong());
        verify(purchaseRepository, never()).save(any());
    }

    @Test
    void refundPurchase_purchaseNotFound_throwsException() {
        Long userId = 1L;
        Long purchaseId = 999L;

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> refundService.refundPurchase(userId, purchaseId));

        assertEquals("Couldn't find the specified purchase", exception.getMessage());
        verify(purchaseRepository, never()).save(any());
    }

    @Test
    void refundPurchase_userMismatch_throwsException() {
        Long requestUserId = 1L;
        Long actualOwnerId = 2L;
        Long purchaseId = 100L;

        User requestingUser = new User();
        requestingUser.setId(requestUserId);

        User actualOwner = new User();
        actualOwner.setId(actualOwnerId);

        Purchase purchase = new Purchase();
        purchase.setId(purchaseId);
        purchase.setUser(actualOwner);

        when(userRepository.findById(requestUserId)).thenReturn(Optional.of(requestingUser));
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchase));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> refundService.refundPurchase(requestUserId, purchaseId));

        assertEquals("You do not have this product, so no refunds :)", exception.getMessage());
        verify(purchaseRepository, never()).save(any());
    }

    @Test
    void refundPurchase_alreadyRefunded_throwsException() {
        Long userId = 1L;
        Long purchaseId = 100L;

        User user = new User();
        user.setId(userId);

        Purchase purchase = new Purchase();
        purchase.setId(purchaseId);
        purchase.setUser(user);
        purchase.setStatus(PurchaseStatus.REFUNDED);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchase));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> refundService.refundPurchase(userId, purchaseId));

        assertEquals("Purchase has already been refunded", exception.getMessage());
        verify(purchaseRepository, never()).save(any());
    }
}