package com.aca.acaonlinewallet.controller;

import com.aca.acaonlinewallet.auth.CurrentUser;
import com.aca.acaonlinewallet.dto.CardDto;
import com.aca.acaonlinewallet.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/wallet_v1/card")
public class CardController {
    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/get/{id}")
    public CardDto getCard(@PathVariable Long id) {
        return cardService.getCard(id);
    }

    @PostMapping("/add")
    public CardDto addCard(@RequestBody CardDto cardDto) {
        return cardService.addCard(cardDto);
    }

    @PutMapping("/update/{id}")
    public CardDto updateCard(@RequestBody CardDto cardDto, @PathVariable Long id) {
        return cardService.updateCard(cardDto, id);
    }

    @PutMapping("/changeDefault/{cardId}")
    public void changeDefaultCard(@PathVariable Long cardId,
                                  @AuthenticationPrincipal CurrentUser currentUser) {
        cardService.changeDefault(cardId, currentUser.getId());
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCard(@PathVariable Long id) {
        cardService.deleteById(id);
    }

}