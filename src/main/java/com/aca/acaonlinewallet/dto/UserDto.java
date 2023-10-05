package com.aca.acaonlinewallet.dto;

import com.aca.acaonlinewallet.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String phoneNumber;
    private Date birthDate;
    private String gender;
    private WalletDto walletDto;
    private String password;
    private List<CardDto> listOfCardDtos;


    public static UserDto mapEntityToDto(User entity) {
        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setAddress(entity.getAddress());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setGender(entity.getGender());
        dto.setBirthDate(entity.getBirthDate());
        dto.setPassword(entity.getPassword());
        dto.setListOfCardDtos(CardDto.mapEntitiesToDtos(entity.getListOfCards()));
        dto.setWalletDto(WalletDto.mapEntityToDto(entity.getWallet()));
        return dto;
    }

    public static User mapDtoToEntity(UserDto userDto) {
        User entity = new User();
        entity.setId(userDto.getId());
        entity.setName(userDto.getName());
        entity.setEmail(userDto.getEmail());
        entity.setAddress(userDto.getAddress());
        entity.setGender(userDto.getGender());
        entity.setPhoneNumber(userDto.getPhoneNumber());
        entity.setBirthDate(userDto.getBirthDate());
        entity.setPassword(userDto.getPassword());
        entity.setListOfCards(CardDto.mapDtosToEntities(userDto.getListOfCardDtos()));
        entity.setWallet(WalletDto.mapDtoToEntity(userDto.getWalletDto()));
        return entity;
    }

    public static List<UserDto> mapEntitiesToDtos(List<User> userList) {
        return userList.stream().map(UserDto::mapEntityToDto).collect(Collectors.toList());
    }

    public static List<User> mapDtosToEntities(List<UserDto> userDtoList) {
        return userDtoList.stream().map(UserDto::mapDtoToEntity).collect(Collectors.toList());
    }

    public enum Role {
        USER("USER"),
        ADMIN("ADMIN");

        private final String value;

        Role(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}