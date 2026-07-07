package org.example.beam.service;

import org.example.beam.enumeration.PurchaseStatus;
import org.example.beam.model.Purchase;
import org.example.beam.model.User;
import org.example.beam.repository.PurchaseRepository;
import org.example.beam.repository.UserRepository;

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
        String email = "owner@email.com";
        Long purchaseId = 100L;

        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        Purchase purchase = new Purchase();
        purchase.setId(purchaseId);
        purchase.setUser(user);
        purchase.setStatus(PurchaseStatus.COMPLETED);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchase));
        when(purchaseRepository.save(any(Purchase.class))).thenAnswer(i -> i.getArguments()[0]);

        Purchase refundedPurchase = refundService.refundPurchase(email, purchaseId);

        assertNotNull(refundedPurchase);
        assertEquals(PurchaseStatus.REFUNDED, refundedPurchase.getStatus());
        verify(purchaseRepository, times(1)).save(purchase);
    }

    @Test
    void refundPurchase_userNotFound_throwsException() {
        String email = "missing@email.com";
        Long purchaseId = 100L;

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> refundService.refundPurchase(email, purchaseId));

        assertEquals("User not found", exception.getMessage());
        verify(purchaseRepository, never()).findById(anyLong());
        verify(purchaseRepository, never()).save(any());
    }

    @Test
    void refundPurchase_purchaseNotFound_throwsException() {
        String email = "owner@email.com";
        Long purchaseId = 999L;

        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> refundService.refundPurchase(email, purchaseId));

        assertEquals("Couldn't find the specified purchase", exception.getMessage());
        verify(purchaseRepository, never()).save(any());
    }

    @Test
    void refundPurchase_userMismatch_throwsException() {
        String requesterEmail = "requester@email.com";
        Long purchaseId = 100L;

        User requestingUser = new User();
        requestingUser.setId(1L);
        requestingUser.setEmail(requesterEmail);

        User actualOwner = new User();
        actualOwner.setId(2L);

        Purchase purchase = new Purchase();
        purchase.setId(purchaseId);
        purchase.setUser(actualOwner);

        when(userRepository.findByEmail(requesterEmail)).thenReturn(Optional.of(requestingUser));
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchase));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> refundService.refundPurchase(requesterEmail, purchaseId));

        assertEquals("This purchase does not belong to you", exception.getMessage());
        verify(purchaseRepository, never()).save(any());
    }

    @Test
    void refundPurchase_alreadyRefunded_throwsException() {
        String email = "owner@email.com";
        Long purchaseId = 100L;

        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        Purchase purchase = new Purchase();
        purchase.setId(purchaseId);
        purchase.setUser(user);
        purchase.setStatus(PurchaseStatus.REFUNDED);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchase));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> refundService.refundPurchase(email, purchaseId));

        assertEquals("Purchase has already been refunded", exception.getMessage());
        verify(purchaseRepository, never()).save(any());
    }
}
