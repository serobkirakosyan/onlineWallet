package com.aca.acaonlinewallet.service;

import com.aca.acaonlinewallet.dto.CardDto;
import com.aca.acaonlinewallet.entity.Card;
import com.aca.acaonlinewallet.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardService {

    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public CardDto getCard(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new RuntimeException("Card by id " + id + " is not found"));
        return CardDto.mapEntityToDto(card);
    }

    @Transactional
    public CardDto addCard(CardDto cardDto) {

        if (cardDto == null) {
            throw new RuntimeException("Card can't be null");
        }

        Card card = cardRepository.save(CardDto.mapDtoToEntity(cardDto));
        return CardDto.mapEntityToDto(card);
    }

    @Transactional
    public CardDto updateCard(CardDto cardDto, Long id) {

        if (cardDto == null ) {
            throw new RuntimeException("Card or id can't be null");
        }
        cardRepository.findById(id).orElseThrow(()->new RuntimeException("Card by id " + id + " is not found"));
        Card card = CardDto.mapDtoToEntity(cardDto);
        card.setId(id);
        return CardDto.mapEntityToDto(cardRepository.save(card));
    }

    @Transactional
    public void deleteById(Long id) {
        boolean isExist = cardRepository.existsById(id);
        if (!isExist) {
            throw new RuntimeException("Card by id " + id + " is not found");
        }

        cardRepository.deleteById(id);
    }

}
