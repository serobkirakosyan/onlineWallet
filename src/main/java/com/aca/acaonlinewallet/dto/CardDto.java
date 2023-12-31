package com.aca.acaonlinewallet.dto;

import com.aca.acaonlinewallet.entity.Card;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CardDto {

    private Long id;

    private String cardHolderName;

    private String cardHolderSurname;

    private Long cardNumber;

    private Date expirationDate;

    private Integer cvv;

    private Double account;

    private Long accountNumber;

    private Boolean isDefault;

    public static CardDto mapEntityToDto(Card card) {
        if (card == null) {
            return null;
        }

        CardDto cardDto = new CardDto();
        cardDto.setId(card.getId());
        cardDto.setCardHolderName(card.getCardHolderName());
        cardDto.setCardHolderSurname(card.getCardHolderSurname());
        cardDto.setCardNumber(card.getCardNumber());
        cardDto.setAccount(card.getAccount());
        cardDto.setCvv(card.getCvv());
        cardDto.setAccountNumber(card.getAccountNumber());
        cardDto.setExpirationDate(card.getExpirationDate());
        cardDto.setIsDefault(card.getIsDefault());
        return cardDto;
    }

    public static Card mapDtoToEntity(CardDto cardDto) {
        if (cardDto == null) {
            return null;
        }

        Card card = new Card();
        card.setId(cardDto.getId());
        card.setCardHolderName(cardDto.getCardHolderName());
        card.setCardHolderSurname(cardDto.getCardHolderSurname());
        card.setCardNumber(cardDto.getCardNumber());
        card.setCvv(cardDto.getCvv());
        card.setAccount(cardDto.getAccount());
        card.setAccountNumber(cardDto.getAccountNumber());
        card.setExpirationDate(cardDto.getExpirationDate());
        card.setIsDefault(cardDto.getIsDefault());
        return card;
    }

    public static List<CardDto> mapEntitiesToDtos(List<Card> cards) {
        if (CollectionUtils.isEmpty(cards)) {
            return new ArrayList<>();
        }

        return cards.stream().map(CardDto::mapEntityToDto).collect(Collectors.toList());
    }

    public static List<Card> mapDtosToEntities(List<CardDto> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            return new ArrayList<>();
        }

        return dtos.stream().map(CardDto::mapDtoToEntity).collect(Collectors.toList());
    }

}