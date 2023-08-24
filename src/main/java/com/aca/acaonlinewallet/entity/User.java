package com.aca.acaonlinewallet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user", schema = "wallet_v1")
@Getter
@Setter
public class User {

    @Id
    @SequenceGenerator(name = "user_id_seq", sequenceName = "wallet_v1.user_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wallet_v1.user_id_seq")
    @Column(nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "age")
    private Integer age;

    @Column(name = "gender")
    private String gender;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Wallet wallet;

}
