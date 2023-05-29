package com.bankinc.bancinc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.bankinc.bancinc.Controller.TransactionController;
import com.bankinc.bancinc.Model.Transaction;
import com.bankinc.bancinc.Service.TransactionService;

public class TestTransactionController {
    
    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPurchase() throws Exception {
        Transaction transaction = new Transaction();
        Transaction newTransaction = new Transaction();

        when(transactionService.purchase(transaction)).thenReturn(newTransaction);

        ResponseEntity<?> response = transactionController.purchase(transaction);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newTransaction, response.getBody());
        verify(transactionService, times(1)).purchase(transaction);
    }


    @Test
    public void testPurchaseInvalidTransaction() throws Exception {
        Transaction transaction = new Transaction();

        when(transactionService.purchase(transaction)).thenReturn(null);

        ResponseEntity<?> response = transactionController.purchase(transaction);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Make sure your card is functional and has enough money", response.getBody());
        verify(transactionService, times(1)).purchase(transaction);
    }

    @Test
    public void testGetTransactionExistingTransaction() throws Exception {
        Long transactionId = 123L;

        Transaction transaction = new Transaction();

        when(transactionService.getTransaction(transactionId)).thenReturn(transaction);

        ResponseEntity<?> response = transactionController.getTransaction(transactionId);
       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transaction, response.getBody());
        verify(transactionService, times(1)).getTransaction(transactionId);
    }

    @Test
    public void testGetTransactionNonExistingTransaction() throws Exception {
        Long transactionId = 123L;

        when(transactionService.getTransaction(transactionId)).thenReturn(null);

        ResponseEntity<?> response = transactionController.getTransaction(transactionId);
       
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The transactionId doesn't exist", response.getBody());
        verify(transactionService, times(1)).getTransaction(transactionId);
    }

    @Test
    public void testAnulation() throws Exception {
        Transaction newTransaction = new Transaction();
        Transaction transaction = new Transaction();

        when(transactionService.anulationTransaction(newTransaction)).thenReturn(transaction);

        ResponseEntity<?> response = transactionController.anulation(newTransaction);
       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transaction, response.getBody());
        verify(transactionService, times(1)).anulationTransaction(newTransaction);
    }

    @Test
    public void testAnulationInvalidTransaction() throws Exception {
        Transaction newTransaction = new Transaction();

        when(transactionService.anulationTransaction(newTransaction)).thenReturn(null);

        ResponseEntity<?> response = transactionController.anulation(newTransaction);
       
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Make sure it is less than 24h and the correct cardId", response.getBody());
        verify(transactionService, times(1)).anulationTransaction(newTransaction);
    }

     
}
