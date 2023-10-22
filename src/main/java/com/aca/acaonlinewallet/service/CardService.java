package com.aca.acaonlinewallet.service;

import com.aca.acaonlinewallet.dto.CardDto;
import com.aca.acaonlinewallet.entity.Card;
import com.aca.acaonlinewallet.entity.User;
import com.aca.acaonlinewallet.exception.CardNotFoundException;
import com.aca.acaonlinewallet.exception.CardOwnershipException;
import com.aca.acaonlinewallet.exception.NullDtoException;
import com.aca.acaonlinewallet.repository.CardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class CardService {
    private final Logger logger = LoggerFactory.getLogger(CardService.class);

    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card getCardByCardNumber(Long cardNumber) {
        logger.info("Getting card by card number: {}", cardNumber);
        return cardRepository.findByCardNumber(cardNumber).orElseThrow(() -> new RuntimeException("Card by id " + cardNumber + " is not found"));
    }

    public CardDto getCard(Long id) {
        logger.info("Getting card by id: {}", id);
        Card card = cardRepository.findById(id).orElseThrow(() -> new RuntimeException("Card by id " + id + " is not found"));
        return CardDto.mapEntityToDto(card);
    }

    @Transactional
    public CardDto addCard(CardDto cardDto) {

        logger.info("Adding a new card: {}", cardDto);
        if (cardDto == null) {
            throw new NullDtoException("Card can't be null");
        }

        Card card = cardRepository.save(CardDto.mapDtoToEntity(cardDto));
        return CardDto.mapEntityToDto(card);
    }

    @Transactional
    public CardDto updateCard(CardDto cardDto, Long id) {

        logger.info("Updating card with id: {}", id);
        if (cardDto == null) {
            throw new NullDtoException("Card or id can't be null");
        }
        cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException("Card by id " + id + " is not found"));
        Card card = CardDto.mapDtoToEntity(cardDto);
        card.setId(id);
        return CardDto.mapEntityToDto(cardRepository.save(card));
    }

    @Transactional
    public void changeDefault(Long cardId, Long userId) {
        logger.info("Changing default card with id: {}", cardId);
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("Card can't be null"));
        if (card.getIsDefault()) {
            return;
        }
        User user = card.getUser();

        if (!Objects.equals(user.getId(), userId)) {
            throw new CardOwnershipException("This card does not belong to this user");
        }

        List<Card> cardList = user.getListOfCards();
        for (Card defaultCard : cardList) {
            if (defaultCard.getIsDefault()) {
                defaultCard.setIsDefault(false);
                cardRepository.save(defaultCard);
                break;
            }
        }

        card.setIsDefault(true);
        cardRepository.save(card);
    }

    @Transactional
    public void deleteById(Long id) {
        logger.info("Deleting card with id: {}", id);
        boolean isExist = cardRepository.existsById(id);
        if (!isExist) {
            throw new CardNotFoundException("Card by id " + id + " is not found");
        }

        cardRepository.deleteById(id);
    }

}
