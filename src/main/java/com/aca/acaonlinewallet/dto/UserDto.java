package com.aca.acaonlinewallet.dto;

import com.aca.acaonlinewallet.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
    private WalletDto wallet;
    private String password;
    private List<CardDto> listOfCardDtos;


    public static UserDto mapEntityToDto(User entity) {
        if (entity == null) {
            return null;
        }

        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setAddress(entity.getAddress());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setGender(entity.getGender());
        dto.setBirthDate(entity.getBirthDate());
        if (!CollectionUtils.isEmpty(entity.getListOfCards())) {
            dto.setListOfCardDtos(CardDto.mapEntitiesToDtos(entity.getListOfCards()));
        }
        if (entity.getWallet() != null) {
            dto.setWallet(WalletDto.mapEntityToDto(entity.getWallet()));
        }
        return dto;
    }

    public static User mapDtoToEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        User entity = new User();
        entity.setId(userDto.getId());
        entity.setName(userDto.getName());
        entity.setEmail(userDto.getEmail());
        entity.setAddress(userDto.getAddress());
        entity.setGender(userDto.getGender());
        entity.setPhoneNumber(userDto.getPhoneNumber());
        entity.setBirthDate(userDto.getBirthDate());
        if (!CollectionUtils.isEmpty(userDto.getListOfCardDtos())) {
            entity.setListOfCards(CardDto.mapDtosToEntities(userDto.getListOfCardDtos()));
        }
        if (userDto.getWallet() != null) {
            entity.setWallet(WalletDto.mapDtoToEntity(userDto.getWallet()));
        }
        return entity;
    }

    public static List<UserDto> mapEntitiesToDtos(List<User> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return new ArrayList<>();
        }

        return userList.stream().map(UserDto::mapEntityToDto).collect(Collectors.toList());
    }

    public static List<User> mapDtosToEntities(List<UserDto> userDtoList) {
        if (CollectionUtils.isEmpty(userDtoList)) {
            return new ArrayList<>();
        }

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