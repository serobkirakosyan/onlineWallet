package com.aca.acaonlinewallet.service;

import com.aca.acaonlinewallet.dto.PaymentDto;
import com.aca.acaonlinewallet.dto.SiteDto;
import com.aca.acaonlinewallet.dto.UserDto;
import com.aca.acaonlinewallet.dto.WalletDto;
import com.aca.acaonlinewallet.entity.Payment;
import com.aca.acaonlinewallet.entity.Site;
import com.aca.acaonlinewallet.entity.User;
import com.aca.acaonlinewallet.entity.Wallet;
import com.aca.acaonlinewallet.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SiteService {
    private final SiteRepository siteRepository;
    private final WalletService walletService;

    public SiteDto register(Long walletId, SiteDto siteDto) {
        WalletDto wallet = walletService.getWallet(walletId);
        siteDto.setWalletDto(wallet);
        Site savedSite = siteRepository.save(SiteDto.mapDtoToEntity(siteDto));
        return SiteDto.mapEntityToDto(savedSite);
    }

    public SiteDto getSiteByName(String siteName) {
        if (siteName == null) {
            throw new RuntimeException("Site name can't be null");
        }
        Site site = siteRepository.findByName(siteName).orElseThrow(() -> new IllegalArgumentException(String.format("No site with name '%s'", siteName)));
        return SiteDto.mapEntityToDto(site);
    }

}
