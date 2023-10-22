package com.aca.acaonlinewallet.dto;

import com.aca.acaonlinewallet.entity.Payment;
import com.aca.acaonlinewallet.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class PaymentDto {
    private Long id;
    private Double amount;
    private String key;
    private Date date;
    private UserDto user;

    public static Payment mapDtoToEntity(PaymentDto dto) {
        if (dto == null) {
            return null;
        }
        Payment entity = new Payment();
        entity.setId(dto.getId());
        entity.setAmount(dto.getAmount());
        entity.setKey(dto.getKey());
        entity.setDate(dto.getDate());
        entity.setUser(UserDto.mapDtoToEntity(dto.getUser()));
        return entity;
    }

    public static PaymentDto mapEntityToDto(Payment entity) {
        if (entity == null) {
            return null;
        }

        PaymentDto dto = new PaymentDto();
        dto.setId(entity.getId());
        dto.setAmount(entity.getAmount());
        dto.setDate(entity.getDate());
        dto.setKey(entity.getKey());
        dto.setUser(UserDto.mapEntityToDto(entity.getUser()));
        return dto;
    }


    public static List<PaymentDto> mapEntitiesToDtos(List<Payment> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return new ArrayList<>();
        }

        return entities.stream().map(PaymentDto::mapEntityToDto).collect(Collectors.toList());
    }

    public static List<Payment> mapDtosToEntities(List<PaymentDto> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            return new ArrayList<>();
        }

        return dtos.stream().map(PaymentDto::mapDtoToEntity).collect(Collectors.toList());
    }

}
