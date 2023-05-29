package com.bankinc.bancinc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bankinc.bancinc.Model.Card;
import com.bankinc.bancinc.Model.Product;
import com.bankinc.bancinc.Model.User;
import com.bankinc.bancinc.Repository.CardRepository;
import com.bankinc.bancinc.Repository.UserRepository;
import com.bankinc.bancinc.Service.CardService;

public class TestCardService {
    
    @Mock
    private UserRepository userRepository;

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testGenerateCardId() {
        Product product = new Product(1L, "Product A");

        when(cardRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Long generatedCardId = cardService.generateCardId(product.getProductId());

        assertNotNull(generatedCardId);
        assertTrue(generatedCardId >= 10000000000L && generatedCardId <= 99999999999L);
        verify(cardRepository).findById(generatedCardId);
    }

    @Test
    public void testGenerateCardId_whenCardAlreadyExists() {
        Product product = new Product(1L, "Product A");

        when(cardRepository.findById(any(Long.class))).thenReturn(Optional.of(new Card()));

        Long generatedCardId = cardService.generateCardId(product.getProductId());

        assertNull(generatedCardId);
        verify(cardRepository).findById(any(Long.class));
    }

    @Test
    public void testActiveCard() {
        Card card = new Card();
        card.setCardId(1234567890L);
        card.setActivateCard(false);

        when(cardRepository.findById(card.getCardId())).thenReturn(Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenReturn(card);

        Card activeCard = cardService.activeCard(card);

        assertNotNull(activeCard);
        assertTrue(activeCard.isActivateCard());
        verify(cardRepository).save(activeCard);
    }

    @Test
    public void testActiveCard_whenCardNotFound() {
        Long cardId = 1234567890L;

        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        Card activeCard = cardService.activeCard(new Card(cardId));

        assertNull(activeCard);
    }

    @Test
    public void testActiveCard_whenCardAlreadyActivated() {
        Card card = new Card();
        card.setCardId(1234567890L);
        card.setActivateCard(true);

        when(cardRepository.findById(card.getCardId())).thenReturn(Optional.of(card));

        Card activeCard = cardService.activeCard(card);

        assertNull(activeCard);
    }

    @Test
    public void testBlockCard() {
        Card card = new Card();
        card.setCardId(1234567890L);
        card.setBlock(false);

        when(cardRepository.findById(card.getCardId())).thenReturn(Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenReturn(card);

        Card blockedCard = cardService.blockCard(card.getCardId());

        assertNotNull(blockedCard);
        assertTrue(blockedCard.isBlock());
        verify(cardRepository).save(blockedCard);
    }

    @Test
    public void testBlockCard_whenCardNotFound() {
        // Arrange
        Long cardId = 1234567890L;

        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        // Act
        Card blockedCard = cardService.blockCard(cardId);

        // Assert
        assertNull(blockedCard);
    }

    @Test
    public void testBlockCard_whenCardAlreadyBlocked() {
        Card card = new Card();
        card.setCardId(1234567890L);
        card.setBlock(true);

        when(cardRepository.findById(card.getCardId())).thenReturn(Optional.of(card));

        Card blockedCard = cardService.blockCard(card.getCardId());

        assertNull(blockedCard);
    }


    @Test
    public void testRechargeCard_whenCardCannotBeUsed() {
        Card card = new Card();
        card.setCardId(1234567890L);
        card.setBlock(true);

        when(cardRepository.findById(card.getCardId())).thenReturn(Optional.of(card));

        BigDecimal rechargeAmount = new BigDecimal("100.00");
        Card rechargeCard = new Card(card.getCardId());
        rechargeCard.setBalance(rechargeAmount);

        Card updatedCard = cardService.rechargeCard(rechargeCard);

        assertNull(updatedCard);
    }



}
