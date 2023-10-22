package com.aca.acaonlinewallet.dto;

import com.aca.acaonlinewallet.entity.Site;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class SiteDto {

    private Long id;
    private String name;
    private String domain;
    private String apiKey;
    private String apiSecret;
    private WalletDto walletDto;

    public static SiteDto mapEntityToDto(Site entity) {
        if (entity == null) {
            return null;
        }

        SiteDto dto = new SiteDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDomain(entity.getDomain());
        dto.setApiKey(entity.getApiKey());
        dto.setApiSecret(entity.getApiSecret());
        if (entity.getWallet() != null) {
            dto.setWalletDto(WalletDto.mapEntityToDto(entity.getWallet()));
        }
        return dto;
    }

    public static Site mapDtoToEntity(SiteDto dto) {
        if (dto == null) {
            return null;
        }

        Site entity = new Site();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDomain(dto.getDomain());
        entity.setApiKey(dto.getApiKey());
        entity.setApiSecret(dto.getApiSecret());
        if (dto.getWalletDto() != null) {
            entity.setWallet(WalletDto.mapDtoToEntity(dto.getWalletDto()));
        }
        return entity;
    }


    public static List<SiteDto> mapEntitiesToDtos(List<Site> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return new ArrayList<>();
        }

        return entities.stream().map(SiteDto::mapEntityToDto).collect(Collectors.toList());
    }

    public static List<Site> mapDtosToEntities(List<SiteDto> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            return new ArrayList<>();
        }

        return dtos.stream().map(SiteDto::mapDtoToEntity).collect(Collectors.toList());
    }


}
