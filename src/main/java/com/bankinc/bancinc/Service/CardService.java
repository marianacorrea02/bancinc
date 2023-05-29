package com.bankinc.bancinc.Service;

import java.math.BigDecimal;
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

    public Card generateCard(User user, Product product) {
        Long randomNumber = generateCardId(product.getProductId());
        User newUser = createUser(user);
        LocalDate dueDate = generateDueDate();
        Card card = new Card();
        card.setCardId(randomNumber);
        card.setProduct(product);
        card.setUser(newUser);
        card.setDueDate(dueDate);
        cardRepository.save(card);
        return card;
    }

    public Long generateCardId(Long id) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int number = random.nextInt(10);
            sb.append(number);
        }
        String randomNumber = id + sb.toString();
        Long number = Long.parseLong(randomNumber);

        if (getCard(number) == null) {
            return number;
        }
        return null;
    }

    public User createUser(User user) {

        User newUser = new User(user.getName(), user.getLastName());

        userRepository.save(newUser);

        return newUser;
    }

    public LocalDate generateDueDate() {

        LocalDate today = LocalDate.now();
        LocalDate dueDate = today.plusYears(3).withDayOfMonth(31);

        return dueDate;
    }

    public Card activeCard(Card card) {

        Card activeCard = getCard(card.getCardId());
        if (activeCard == null || isActivate(activeCard)) {
            return null;
        } else {
            activeCard.setActivateCard(true);
            cardRepository.save(activeCard);
            return activeCard;
        }
    }

    public Card blockCard(Long id) {
        Card blockCard = cardRepository.findById(id).orElse(null);
        if (blockCard == null || isBlock(blockCard)) {
            return null;
        } else {
            blockCard.setBlock(true);
            cardRepository.save(blockCard);
            return blockCard;
        }
    }

    public Card rechargeCard(Card card) {
        Card rechargeCard = getCard(card.getCardId());
        if(rechargeCard == null || isBlock(rechargeCard) || isActivate(rechargeCard)==false || moreThanZero(card.getBalance()) == false ){
            System.out.println(moreThanZero(card.getBalance()));
            return null;
        }else{
        rechargeCard.setBalance(rechargeCard.getBalance().add(card.getBalance()));
        cardRepository.save(rechargeCard);
        return rechargeCard;
        }
    }

    public Card getCard(Long cardId) {
        Card card = cardRepository.findById(cardId).orElse(null);

        return card;
    }

    public boolean isActivate(Card card) {
        if (card.isActivateCard() == true) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isBlock(Card card) {
        if (card.isBlock() == true) {
            return true;
        } else {
            return false;
        }
    }

    public boolean moreThanZero(BigDecimal balance) {
        if (balance.compareTo(BigDecimal.ZERO) > 0) {
            return true;
        } else {
            return false;
        }
    }
}
