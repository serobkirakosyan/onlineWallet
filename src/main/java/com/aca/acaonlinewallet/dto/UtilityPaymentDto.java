package com.aca.acaonlinewallet.dto;

import com.aca.acaonlinewallet.entity.UtilityPayment;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class UtilityPaymentDto {

    private Long id;
    private UtilityPaymentType type;
    private Double amountDue;
    private Double paidAmount;
    private Boolean isPaid;
    private Date addedDate;
    private Date paidDate;
    private UserDto userDto;

    public static UtilityPayment mapDtoToEntity(UtilityPaymentDto dto) {
        if (dto == null) {
            return null;
        }

        UtilityPayment entity = new UtilityPayment();
        entity.setId(dto.getId());
        entity.setType(dto.getType().getValue());
        entity.setAmountDue(dto.getAmountDue());
        entity.setPaidAmount(dto.getPaidAmount());
        entity.setIsPaid(dto.getIsPaid());
        entity.setAddedDate(dto.getAddedDate());
        entity.setPaidDate(dto.getAddedDate());
        if (dto.getUserDto() != null) {
            entity.setUser(UserDto.mapDtoToEntity(dto.getUserDto()));
        }

        return entity;
    }

    public static UtilityPaymentDto mapEntityToDto(UtilityPayment entity) {
        if (entity == null) {
            return null;
        }

        UtilityPaymentDto dto = new UtilityPaymentDto();
        dto.setId(entity.getId());
        dto.setType(UtilityPaymentType.fromValue(entity.getType()));
        dto.setAmountDue(entity.getAmountDue());
        dto.setPaidAmount(entity.getPaidAmount());
        dto.setIsPaid(entity.getIsPaid());
        dto.setAddedDate(entity.getAddedDate());
        dto.setPaidDate(entity.getPaidDate());
        if (dto.getUserDto() != null) {
            dto.setUserDto(UserDto.mapEntityToDto(entity.getUser()));
        }

        return dto;
    }

    public static List<UtilityPayment> mapDtosToEntities(List<UtilityPaymentDto> dtos) {
        return dtos.stream().map(UtilityPaymentDto::mapDtoToEntity).collect(Collectors.toList());
    }

    public static List<UtilityPaymentDto> mapEntitiesToDtos(List<UtilityPayment> entities) {
        return entities.stream().map(UtilityPaymentDto::mapEntityToDto).collect(Collectors.toList());
    }

    public enum UtilityPaymentType {
        NATURAL_GAS("Natural Gas"),
        WATER("Water"),
        ELECTRICITY("Electricity"),
        INTERNET("Internet"),
        PHONE("Phone");

        private final String value;

        UtilityPaymentType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static UtilityPaymentType fromValue(String value) {
            for (UtilityPaymentType type : UtilityPaymentType.values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }

            throw new IllegalArgumentException(String.format("No enum constant matches the value: '%s'", value));
        }
    }

}