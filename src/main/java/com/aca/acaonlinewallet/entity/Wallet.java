package com.aca.acaonlinewallet.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "wallet", schema = "wallet_v1")
@Data
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "number")
    private String number;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
