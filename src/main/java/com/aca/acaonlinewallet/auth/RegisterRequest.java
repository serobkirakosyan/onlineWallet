package com.aca.acaonlinewallet.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String phoneNumber;
    private Date birthDate;
    private String gender;
    private String password;

}