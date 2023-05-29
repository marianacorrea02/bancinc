package com.bankinc.bancinc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bankinc.bancinc.Controller.CardController;
import com.bankinc.bancinc.Model.Card;
import com.bankinc.bancinc.Model.Product;
import com.bankinc.bancinc.Model.User;
import com.bankinc.bancinc.Repository.ProductRepository;
import com.bankinc.bancinc.Service.CardService;

public class TestCardController {

    @Mock
    private CardService cardService;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CardController cardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void generateCardIdExistingProduct() {

        Long productId = 1L;
        User user = new User();
        Product product = new Product();
        Card expectedCard = new Card();

        when(productRepository.findById(productId)).thenReturn(java.util.Optional.of(product));
        when(cardService.generateCard(user, product)).thenReturn(expectedCard);

        ResponseEntity<?> response = cardController.generateCardId(productId, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCard, response.getBody());
        verify(productRepository, times(1)).findById(productId);
        verify(cardService, times(1)).generateCard(user, product);
    }

    @Test
    void generateCardIdNonExistingProduct() {
        Long productId = 1L;
        User user = new User();

        when(productRepository.findById(productId)).thenReturn(java.util.Optional.empty());

        ResponseEntity<?> response = cardController.generateCardId(productId, user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Product doesn't exist", response.getBody());
        verify(productRepository, times(1)).findById(productId);
        verify(cardService, never()).generateCard(any(), any());
    }

    @Test
    void activateCardValidCard() {
        Card card = new Card();
        Card activeCard = new Card();

        when(cardService.activeCard(card)).thenReturn(activeCard);

        ResponseEntity<?> response = cardController.activateCard(card);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(activeCard, response.getBody());
        verify(cardService, times(1)).activeCard(card);
    }

    @Test
    void activateCardInvalidCard() {
        Card card = new Card();

        when(cardService.activeCard(card)).thenReturn(null);

        ResponseEntity<?> response = cardController.activateCard(card);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The card don't exist or is already activate", response.getBody());
        verify(cardService, times(1)).activeCard(card);
    }

    @Test
    void blockCardExistingCard() {
        Long cardId = 1L;
        Card card = new Card();

        when(cardService.blockCard(cardId)).thenReturn(card);

        ResponseEntity<?> response = cardController.blockCard(cardId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(card, response.getBody());
        verify(cardService, times(1)).blockCard(cardId);
    }

    @Test
    void blockCardNonExistingCard() {
        Long cardId = 1L;

        when(cardService.blockCard(cardId)).thenReturn(null);

        ResponseEntity<?> response = cardController.blockCard(cardId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The card don't exist or is already blocked", response.getBody());
        verify(cardService, times(1)).blockCard(cardId);
    }

    @Test
    void rechargeCardValidCard() {
        Card card = new Card();
        Card newCard = new Card();

        when(cardService.rechargeCard(card)).thenReturn(newCard);

        ResponseEntity<?> response = cardController.rechargeCard(card);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newCard, response.getBody());
        verify(cardService, times(1)).rechargeCard(card);
    }

    @Test
    void rechargeCardInvalidCard() {
        Card card = new Card();

        when(cardService.rechargeCard(card)).thenReturn(null);

        ResponseEntity<?> response = cardController.rechargeCard(card);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("You can't recharge, make sure is active and unblock or more than 0", response.getBody());
        verify(cardService, times(1)).rechargeCard(card);
    }

    @Test
    void checkBalanceExistingCard() {
        Long cardId = 1L;
        Card card = new Card();
        card.setBalance(new BigDecimal(100));

        when(cardService.getCard(cardId)).thenReturn(card);

        ResponseEntity<?> response = cardController.checkBalance(cardId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(card.getBalance(), response.getBody());
        verify(cardService, times(1)).getCard(cardId);
    }

    @Test
    void checkBalanceNonExistingCard() {
        Long cardId = 1L;

        when(cardService.getCard(cardId)).thenReturn(null);

        ResponseEntity<?> response = cardController.checkBalance(cardId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The card don't exist", response.getBody());
        verify(cardService, times(1)).getCard(cardId);
    }

}
