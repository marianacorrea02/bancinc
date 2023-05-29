package com.bankinc.bancinc.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankinc.bancinc.Model.Card;
import com.bankinc.bancinc.Model.Product;
import com.bankinc.bancinc.Model.User;
import com.bankinc.bancinc.Repository.ProductRepository;
import com.bankinc.bancinc.Service.CardService;

@RestController
@RequestMapping("/card")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/{id}/number")
    public ResponseEntity<?> generateCardId(@PathVariable("id") Long id, @RequestBody User user) {

        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return ResponseEntity.badRequest().body("Product doesn't exist");
        } else {
            Card card = cardService.generateCard(id, user, product);
            return ResponseEntity.ok(card);
        }

    }

    @PostMapping("/enroll")
    public ResponseEntity<?> activateCard(@RequestBody Card card) {

        Card activeCard = cardService.activeCard(card);
        if (activeCard == null) {
            return ResponseEntity.badRequest().body("The card don't exist or is already activate");
        } else {
            return ResponseEntity.ok(activeCard);
        }

    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<?> blockCard(@PathVariable("cardId") Long cardId) {
        Card card = cardService.blockCard(cardId);
        if (card == null) {
            return ResponseEntity.badRequest().body("The card don't exist or is already blocked");
        } else {
            return ResponseEntity.ok(card);
        }
    }

    @PostMapping("/balance")
    public ResponseEntity<?> rechargeCard(@RequestBody Card card) {
        Card newCard = cardService.rechargeCard(card);
        if (newCard == null) {
            return ResponseEntity.badRequest().body("You can't recharge, make sure is active and unblock or more than 0");
        } else {
            return ResponseEntity.ok(card);
        }
    }

    @GetMapping("/balance/{cardId}")
    public ResponseEntity<?> checkBalance(@PathVariable("cardId") Long cardId) {
        Card card = cardService.getCard(cardId);
        if (card == null) {
            return ResponseEntity.badRequest().body("The card don't exist");
        } else {
            return ResponseEntity.ok(card.getBalance());
        }
        

    }

}
