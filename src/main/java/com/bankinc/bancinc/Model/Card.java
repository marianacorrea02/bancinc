package com.bankinc.bancinc.Model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "card")
public class Card {

    @Id
    private Long cardId;

    @Column
    private LocalDate dueDate;

    @Column
    private boolean activateCard;

    @Column
    private BigDecimal balance = BigDecimal.ZERO;

    @Column
    private boolean block;

    @ManyToOne
    @JoinColumn(name = "prosuct_id")
    private Product product;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Card() {
    }

    public Card(Long cardId) {
        this.cardId = cardId;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean isActivateCard() {
        return activateCard;
    }

    public void setActivateCard(boolean activateCard) {
        this.activateCard = activateCard;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }



    

    
    
    
}
