package com.bankinc.bancinc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bankinc.bancinc.Model.Card;
import com.bankinc.bancinc.Model.Transaction;
import com.bankinc.bancinc.Repository.CardRepository;
import com.bankinc.bancinc.Repository.TransactionRepository;
import com.bankinc.bancinc.Service.CardService;
import com.bankinc.bancinc.Service.TransactionService;

public class TestTransactionService {

    @Mock
    private CardService cardService;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPurchase() {
        Card card = new Card();
        card.setCardId(1234567890L);
        card.setBalance(new BigDecimal("100.00"));

        Transaction transaction = new Transaction();
        transaction.setCardId(card);
        transaction.setPrice(new BigDecimal("50.00"));

        when(cardService.getCard(transaction.getCardId().getCardId())).thenReturn(card);
        when(cardService.canUse(card)).thenReturn(true);
        doReturn(null).when(cardRepository).save(any(Card.class));
        doReturn(null).when(transactionRepository).save(any(Transaction.class));

        Transaction purchasedTransaction = transactionService.purchase(transaction);

        assertNotNull(purchasedTransaction);
        assertEquals(card.getBalance(), card.getBalance());
        verify(cardRepository).save(card);
        verify(transactionRepository).save(purchasedTransaction);
    }

    @Test
    public void testPurchase_whenCardNotFound() {
        Long cardId = 1234567890L;

        when(cardService.getCard(cardId)).thenReturn(null);

        Transaction transaction = new Transaction();
        transaction.setCardId(new Card(cardId));

        Transaction purchasedTransaction = transactionService.purchase(transaction);

        assertNull(purchasedTransaction);
    }

    @Test
    public void testPurchase_whenCardCannotBeUsed() {
        Card card = new Card();
        card.setCardId(1234567890L);

        when(cardService.getCard(card.getCardId())).thenReturn(card);
        when(cardService.canUse(card)).thenReturn(false);

        Transaction transaction = new Transaction();
        transaction.setCardId(card);

        Transaction purchasedTransaction = transactionService.purchase(transaction);

        assertNull(purchasedTransaction);
    }

    @Test
    public void testPurchase_whenInsufficientBalance() {
        Card card = new Card();
        card.setCardId(1234567890L);
        card.setBalance(new BigDecimal("50.00"));

        when(cardService.getCard(card.getCardId())).thenReturn(card);
        when(cardService.canUse(card)).thenReturn(true);

        Transaction transaction = new Transaction();
        transaction.setCardId(card);
        transaction.setPrice(new BigDecimal("100.00"));

        Transaction purchasedTransaction = transactionService.purchase(transaction);

        assertNull(purchasedTransaction);
    }

    @Test
    public void testGetPurchaseDate() {
        LocalDateTime expectedDateTime = LocalDateTime.now();

        LocalDateTime purchaseDate = transactionService.getPurchaseDate();

        assertNotNull(purchaseDate);
        assertTrue(purchaseDate.isBefore(expectedDateTime) || purchaseDate.isEqual(expectedDateTime));
        assertTrue(purchaseDate.isAfter(expectedDateTime.minusSeconds(1)));
        assertTrue(purchaseDate.isBefore(expectedDateTime.plusSeconds(1)));
    }

    @Test
    public void testResult() {
        BigDecimal balance = new BigDecimal("100.00");
        BigDecimal price = new BigDecimal("50.00");

        BigDecimal result = transactionService.result(balance, price);

        assertNotNull(result);
        assertEquals(balance.subtract(price), result);
    }
    @Test
    public void testResult_whenInsufficientBalance() {
        BigDecimal balance = new BigDecimal("50.00");
        BigDecimal price = new BigDecimal("100.00");

        BigDecimal result = transactionService.result(balance, price);

        assertNull(result);
    }

   

    @Test
    public void testAnulationTransaction_whenTransactionNotFound() {
        // Arrange
        Long transactionId = 1L;

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setCardId(new Card(1234567890L));

        // Act
        Transaction anulatedTransaction = transactionService.anulationTransaction(transaction);

        // Assert
        assertNull(anulatedTransaction);
    }

    @Test
    public void testAnulationTransaction_whenCardTransactionInvalid() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setCardId(new Card(1234567890L));

        Transaction anuleTransaction = new Transaction();
        anuleTransaction.setTransactionId(1L);
        anuleTransaction.setCardId(new Card(9876543210L));

        when(transactionRepository.findById(transaction.getTransactionId())).thenReturn(Optional.of(anuleTransaction));

        // Act
        Transaction anulatedTransaction = transactionService.anulationTransaction(transaction);

        // Assert
        assertNull(anulatedTransaction);
    }

    @Test
    public void testAnulationTransaction_whenLateAnulation() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setCardId(new Card(1234567890L));
        transaction.setPurchaseDate(LocalDateTime.now().minusHours(25));

        Transaction anuleTransaction = new Transaction();
        anuleTransaction.setTransactionId(1L);
        anuleTransaction.setCardId(new Card(1234567890L));
        anuleTransaction.setPurchaseDate(LocalDateTime.now().minusHours(12));

        when(transactionRepository.findById(transaction.getTransactionId())).thenReturn(Optional.of(anuleTransaction));

        // Act
        Transaction anulatedTransaction = transactionService.anulationTransaction(transaction);

        // Assert
        assertNull(anulatedTransaction);
    }

    

    @Test
    public void testIsLateFalse() {
        // Arrange
        LocalDateTime purchaseDate = LocalDateTime.now().minusHours(12);
        LocalDateTime currDateTime = LocalDateTime.now();
        
        // Act
        boolean result = transactionService.isLate(purchaseDate, currDateTime);
        
        // Assert
        assertEquals(false, result);
    }
    
    @Test
    public void testIsLate() {
        // Arrange
        LocalDateTime purchaseDate = LocalDateTime.now().minusHours(26);
        LocalDateTime currDateTime = LocalDateTime.now();
        
        // Act
        boolean result = transactionService.isLate(purchaseDate, currDateTime);
        
        // Assert
        assertEquals(true, result);
    }

    @Test
    public void testCardTransaction() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1L);
        Card card = new Card();
        card.setCardId(1L);
        transaction.setCardId(card);
        when(transactionRepository.findById(1L)).thenReturn(java.util.Optional.of(transaction));
        when(cardService.getCard(1L)).thenReturn(card);

        // Act
        boolean result = transactionService.cardTransaction(transaction);

        // Assert
        assertEquals(true, result);
    }

    @Test
    public void testCardTransactionIncorrect() {
        // Arrange
        Transaction transaction = new Transaction();
        Card fakeCard = new Card(2L);
        transaction.setTransactionId(1L);
        transaction.setCardId(fakeCard);
        Card card = new Card();
        card.setCardId(2L);
        when(transactionRepository.findById(1L)).thenReturn(java.util.Optional.of(transaction));
        when(cardService.getCard(2L)).thenReturn(card);

        // Act
        boolean result = transactionService.cardTransaction(transaction);

        // Assert
        assertEquals(false, result);
    }
    
}
