package com.aca.acaonlinewallet.controller;

import com.aca.acaonlinewallet.dto.CardDto;
import com.aca.acaonlinewallet.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/card")
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

    @PutMapping("/changeDefault/{cardId}/{userId}")
    public void changeDefaultCard(@PathVariable Long cardId, @PathVariable Long userId) {
        cardService.changeDefault(cardId, userId);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCard(@PathVariable Long id) {
        cardService.deleteById(id);
    }

}
