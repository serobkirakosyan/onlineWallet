package com.aca.acaonlinewallet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "card", schema = "wallet_v1")
@Getter
@Setter
public class Card {

    @Id
    @SequenceGenerator(name = "card_id_seq", sequenceName = "wallet_v1.card_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wallet_v1.card_id_seq")
    @Column(nullable = false)
    private Long id;

    @Column(name = "cardholder_name")
    private String cardHolderName;

    @Column(name = "cardholder_surname")
    private String cardHolderSurname;

    @Column(name = "card_number", unique = true)
    private Long cardNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "cvv")
    private Integer cvv;

    @Column(name = "account")
    private Double account;

    @Column(name = "account_number")
    private Long accountNumber;

    @Column(name = "isDefault")
    private Boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
