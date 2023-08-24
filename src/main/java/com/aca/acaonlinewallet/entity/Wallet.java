package com.aca.acaonlinewallet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "wallet", schema = "wallet_v1")
@Getter
@Setter
public class Wallet {

    @Id
    @SequenceGenerator(name = "wallet_id_seq", sequenceName = "wallet_v1.wallet_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wallet_v1.wallet_id_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "balance")
    private Double balance;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
    private List<Card> listOfCard;

}
