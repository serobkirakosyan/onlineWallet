package com.aca.acaonlinewallet.serviceTest;

import com.aca.acaonlinewallet.dto.CardDto;
import com.aca.acaonlinewallet.entity.Card;
import com.aca.acaonlinewallet.entity.User;
import com.aca.acaonlinewallet.repository.CardRepository;
import com.aca.acaonlinewallet.service.CardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    CardRepository cardRepository;

    @InjectMocks
    CardService cardService;

    @Test
    void getCardTestWhenIdIsNotFound() {
        Long id = 1L;
        when(cardRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> cardService.getCard(id));
    }

    @Test
    void getCardTestWhenIdIsNull() {
        when(cardRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> cardService.getCard(null));
    }

    @Test
    void getCardTestWhenIdIsCorrect() {
        Long id = 1L;
        Date date = new Date(2024, 12, 0);
        Card card = new Card();
        card.setId(id);
        card.setCardHolderName("Serob");
        card.setCardHolderSurname("Kirakosyan");
        card.setCardNumber(545454L);
        card.setCvv(548);
        card.setAccount(544.0);
        card.setAccountNumber(5456615L);
        card.setExpirationDate(date);
        card.setIsDefault(true);
        when(cardRepository.findById(id)).thenReturn(Optional.of(card));

        CardDto result = cardService.getCard(id);
        assertEquals(card, CardDto.mapDtoToEntity(result));
    }

    @Test
    void addCardTestWhenDtoIsNull() {
        assertThrows(RuntimeException.class, () -> cardService.addCard(null));
    }

    @Test
    void addCardTestWhenDtoIsNotNull() {
        Long id = 1L;
        Date date = new Date(2024, 12, 0);

        Card card = new Card();
        card.setId(id);
        card.setCardHolderName("Serob");
        card.setCardHolderSurname("Kirakosyan");
        card.setCardNumber(545454L);
        card.setCvv(548);
        card.setAccount(544.0);
        card.setAccountNumber(5456615L);
        card.setExpirationDate(date);
        card.setIsDefault(true);

        when(cardRepository.save(any(Card.class))).thenReturn(card);

        CardDto result = cardService.addCard(CardDto.mapEntityToDto(card));
        assertEquals(card, CardDto.mapDtoToEntity(result));
    }

    @Test
    void updateCardTestWhenIdAndDtoIsCorrect() {
        Long id = 1L;
        Date date = new Date(2024, 12, 0);
        CardDto cardDto = new CardDto();
        cardDto.setId(id);
        cardDto.setCardHolderName("Serob");
        cardDto.setCardHolderSurname("Kirakosyan");
        cardDto.setCardNumber(545454L);
        cardDto.setCvv(548);
        cardDto.setAccount(544.0);
        cardDto.setAccountNumber(5456615L);
        cardDto.setExpirationDate(date);
        cardDto.setIsDefault(true);

        when(cardRepository.findById(id)).thenReturn(Optional.of(CardDto.mapDtoToEntity(cardDto)));
        when(cardRepository.save(any(Card.class))).thenReturn(CardDto.mapDtoToEntity(cardDto));

        CardDto result = cardService.updateCard(cardDto, id);
        assertEquals(CardDto.mapDtoToEntity(result), CardDto.mapDtoToEntity(cardDto));
    }

    @Test
    void updateCardTestWhenDtoIsNull() {
        Long id = 1L;
        assertThrows(RuntimeException.class, () -> cardService.updateCard(null, id));
    }

    @Test
    void updateCardTestWhenIdIsIncorrect() {
        Long id = 1L;
        Date date = new Date(2024, 12, 0);
        CardDto cardDto = new CardDto();
        cardDto.setId(id);
        cardDto.setCardHolderName("Serob");
        cardDto.setCardHolderSurname("Kirakosyan");
        cardDto.setCardNumber(545454L);
        cardDto.setCvv(548);
        cardDto.setAccount(544.0);
        cardDto.setAccountNumber(5456615L);
        cardDto.setExpirationDate(date);
        cardDto.setIsDefault(true);

        when(cardRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cardService.updateCard(cardDto, id));
    }

    @Test
    void changeDefaultTestWhenCardIdIsIncorrect() {
        when(cardRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> cardService.changeDefault(2L, 1L));
    }

    @Test
    void changeDefaultWhenCardIdAndUserIdIsNotEquals() {
        Long cardId = 1L;
        List<Card> cardList = new ArrayList<>();
        Date date = new Date(2024, 12, 0);

        Card card = new Card();
        card.setId(cardId);
        card.setCardHolderName("Serob");
        card.setCardHolderSurname("Kirakosyan");
        card.setCardNumber(545454L);
        card.setCvv(548);
        card.setAccount(544.0);
        card.setAccountNumber(5456615L);
        card.setExpirationDate(date);
        card.setIsDefault(false);
        User user = new User();
        user.setId(5L);
        user.setListOfCards(cardList);
        card.setUser(user);
        cardList.add(card);
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        assertThrows(RuntimeException.class, () -> cardService.changeDefault(cardId, 3L));
    }

    @Test
    void changeDefaultWhenUserIdAndCardIdIsCorrect() {
        Long cardId = 1L;
        Date date = new Date(2024, 12, 0);
        List<Card> cardList = new ArrayList<>();

        User user = new User();
        user.setId(2L);
        user.setListOfCards(cardList);

        Card card = new Card();
        cardList.add(card);
        card.setId(cardId);
        card.setCardHolderName("Serob");
        card.setCardHolderSurname("Kirakosyan");
        card.setCardNumber(545454L);
        card.setCvv(548);
        card.setAccount(544.0);
        card.setAccountNumber(5456615L);
        card.setExpirationDate(date);
        card.setIsDefault(false);
        card.setUser(user);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        cardService.changeDefault(cardId, 2L);
        assert (card.getIsDefault());
    }

}
