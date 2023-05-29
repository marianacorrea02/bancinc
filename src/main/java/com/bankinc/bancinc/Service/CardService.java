package com.bankinc.bancinc.Service;

import java.time.LocalDate;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankinc.bancinc.Model.Card;
import com.bankinc.bancinc.Model.Product;
import com.bankinc.bancinc.Model.User;
import com.bankinc.bancinc.Repository.CardRepository;
import com.bankinc.bancinc.Repository.UserRepository;

@Service
public class CardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardRepository cardRepository;

    public Card generateCard(Long productId, User user, Product product) {
        Long randomNumber = generateCardId(productId);
        User newUser = createUser(user); 
        LocalDate dueDate = generateDueDate();
        Card card =new Card();
        card.setCardId(randomNumber);
        card.setProduct(product);
        card.setUser(newUser);
        card.setDueDate(dueDate);
        cardRepository.save(card);
        return card;
    }

    public Long generateCardId(Long id){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int number = random.nextInt(10);
            sb.append(number);
        }
        String randomNumber = id + sb.toString();
        Long number = Long.parseLong(randomNumber);

        return number;

    }

    public User createUser(User user){
        
        User newUser = new User(user.getName(),user.getLastName());

        userRepository.save(newUser);

        return newUser;
    }

    public LocalDate generateDueDate(){

        LocalDate today = LocalDate.now();
        LocalDate dueDate = today.plusYears(3).withDayOfMonth(31);

        return dueDate;
    }

    public Card activeCard(Card card){

        Card activeCard = cardRepository.findById(card.getCardId()).orElse(null);
        activeCard.setActivateCard(true);
        cardRepository.save(activeCard);
        return activeCard;
    } 

    public Card blockCard(Long id){
        Card blockCard = cardRepository.findById(id).orElse(null);
        blockCard.setBlock(true);
        cardRepository.save(blockCard);
        return blockCard;
    }

    public Card rechargeCard(Card card){
        Card rechargeCard = cardRepository.findById(card.getCardId()).orElse(null);
        rechargeCard.setBalance(card.getBalance());
        cardRepository.save(rechargeCard);
        return rechargeCard ;
    }

    public Card getCard(Long cardId) {
        Card card = cardRepository.findById(cardId).orElse(null);
        return card;
    }
}
