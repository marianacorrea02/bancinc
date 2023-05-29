package com.bankinc.bancinc.Service;

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
    private CardRepository cardRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction purchase(Transaction transaction){
        Card card = cardRepository.findById(transaction.getCardId().getCardId()).orElse(null);
        LocalDateTime dateTime = getPurchaseDate();
        Transaction newTransaction = new Transaction();
        newTransaction.setCardId(card);
        newTransaction.setPrice(transaction.getPrice());
        newTransaction.setPurchaseDate(dateTime);
        card.setBalance(card.getBalance().subtract(transaction.getPrice()));
        transactionRepository.save(newTransaction);
        return newTransaction;
    }

    public LocalDateTime getPurchaseDate(){
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime;
    }

    public Transaction getTransaction(Long id){
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        return transaction;
    }

    public Transaction anulationTransaction (Transaction transaction){
        Transaction anuleTransaction = getTransaction(transaction.getTransactionId());
        Card card = cardRepository.findById(transaction.getCardId().getCardId()).orElse(null);
        boolean aceptTime = isLate(anuleTransaction.getPurchaseDate());
        if (aceptTime == true){
            anuleTransaction.setAnulate(true);
            card.setBalance(card.getBalance().add(transaction.getPrice()));
            transactionRepository.save(anuleTransaction);

            return anuleTransaction;
        }else{
            return null;
        }
    }

    public boolean isLate(LocalDateTime purchaseDate){
        LocalDateTime currDateTime = getPurchaseDate();
        long hoursDifference = ChronoUnit.HOURS.between(purchaseDate, currDateTime);
        if (hoursDifference > 24){
            return false;
        }else{
            return true;
        }

    }
    
}
