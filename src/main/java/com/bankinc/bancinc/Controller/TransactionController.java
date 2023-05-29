package com.bankinc.bancinc.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankinc.bancinc.Model.Transaction;
import com.bankinc.bancinc.Service.TransactionService;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/purchase")
    public ResponseEntity<?> purchase(@RequestBody Transaction transaction) {
        Transaction newTransaction = transactionService.purchase(transaction);
        return ResponseEntity.ok(newTransaction);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<?> getTransaction(@PathVariable("transactionId") Long transactionId) {
        Transaction transaction = transactionService.getTransaction(transactionId);
        if (transaction == null) {
            return ResponseEntity.badRequest().body("The transactionId doesn't exist");
        } else {
            return ResponseEntity.ok(transaction);
        }

    }

    @PostMapping("/anulation")
    public ResponseEntity<?> anulation(@RequestBody Transaction newTransaction) {
        Transaction transaction = transactionService.getTransaction(newTransaction.getTransactionId());
        if (transaction == null) {
            return ResponseEntity.badRequest().body("The transactionId doesn't exist");
        } else {
            transaction = transactionService.anulationTransaction(transaction);
            return ResponseEntity.ok(transaction);
        }
    }

    
}
