package com.aca.acaonlinewallet.dto;

import com.aca.acaonlinewallet.entity.Wallet;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class WalletDto {

    private Long id;
    private Double balance;
    private String number;

    public static WalletDto mapEntityToDto(Wallet entity) {
        if (entity == null) {
            return null;
        }

        WalletDto dto = new WalletDto();
        dto.setId(entity.getId());
        dto.setNumber(entity.getNumber());
        dto.setBalance(entity.getBalance());
        return dto;
    }

    public static Wallet mapDtoToEntity(WalletDto walletDto) {
        if (walletDto == null) {
            return null;
        }

        Wallet entity = new Wallet();
        entity.setBalance(walletDto.getBalance());
        entity.setNumber(walletDto.getNumber());
        entity.setId(walletDto.getId());
        return entity;
    }

    public static List<WalletDto> mapEntitiesToDtos(List<Wallet> walletList) {
        if (CollectionUtils.isEmpty(walletList)) {
            return new ArrayList<>();
        }

        return walletList.stream().map(WalletDto::mapEntityToDto).collect(Collectors.toList());
    }

    public static List<Wallet> mapDtosToEntities(List<WalletDto> walletDtoList) {
        if (CollectionUtils.isEmpty(walletDtoList)) {
            return new ArrayList<>();
        }

        return walletDtoList.stream().map(WalletDto::mapDtoToEntity).collect(Collectors.toList());
    }

}
