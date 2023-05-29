package com.bankinc.bancinc.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankinc.bancinc.Model.Card;
import com.bankinc.bancinc.Model.Transaction;
import com.bankinc.bancinc.Repository.CardRepository;
import com.bankinc.bancinc.Repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private CardService cardService;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction purchase(Transaction transaction) {

        Card card = cardService.getCard(transaction.getCardId().getCardId());
        if (card == null || cardService.canUse(card) == false) {
            return null;
        } else {
            double total = card.getBalance().compareTo(transaction.getPrice());
            if (total < 0) {
                return null;
            } else {

                LocalDateTime dateTime = getPurchaseDate();
                Transaction newTransaction = new Transaction();
                newTransaction.setCardId(card);
                newTransaction.setPrice(transaction.getPrice());
                newTransaction.setPurchaseDate(dateTime);
                card.setBalance(card.getBalance().subtract(transaction.getPrice()));
                cardRepository.save(card);
                transactionRepository.save(newTransaction);
                return newTransaction;
            }
        }
    }

    public LocalDateTime getPurchaseDate() {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime;
    }

    public BigDecimal result(BigDecimal balance, BigDecimal price) {
        double total = balance.compareTo(price);
        if (total < 0) {
            return null;
        } else {
            return balance.subtract(price);
        }
    }

    public Transaction getTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        return transaction;
    }

    public Transaction anulationTransaction(Transaction transaction) {
        Transaction anuleTransaction = getTransaction(transaction.getTransactionId());
        Card card = cardRepository.findById(transaction.getCardId().getCardId()).orElse(null);
        if (anuleTransaction == null || cardTransaction(transaction) == false || isLate(anuleTransaction.getPurchaseDate())) {
            return null;
        } else {

            anuleTransaction.setAnulate(true);
            card.setBalance(card.getBalance().add(anuleTransaction.getPrice()));
            cardRepository.save(card);
            transactionRepository.save(anuleTransaction);

            return anuleTransaction;
        }
    }

    public boolean isLate(LocalDateTime purchaseDate) {
        LocalDateTime currDateTime = getPurchaseDate();
        long hoursDifference = ChronoUnit.HOURS.between(purchaseDate, currDateTime);
        if (hoursDifference > 24) {
            return true;
        } else {
            return false;
        }

    }

    public boolean cardTransaction(Transaction transaction) {
        System.out.println(transaction.getCardId().getCardId());
        Transaction newTransaction = getTransaction(transaction.getTransactionId());
        Card card = cardService.getCard(transaction.getCardId().getCardId());
        if (newTransaction.getCardId().equals(card)) {
            System.out.println(newTransaction.getCardId().getCardId()+"hollla"+card.getCardId());
            return true;
        } else {
            return false;
        }
    }

}
