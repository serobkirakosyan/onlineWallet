package com.aca.acaonlinewallet.service;

import com.aca.acaonlinewallet.dto.*;
import com.aca.acaonlinewallet.entity.Payment;
import com.aca.acaonlinewallet.entity.Site;
import com.aca.acaonlinewallet.entity.User;
import com.aca.acaonlinewallet.entity.Wallet;
import com.aca.acaonlinewallet.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final SiteService siteService;
    private final WalletService walletService;
    private final UserService userService;

    public PaymentDto addPayment(PaymentDto paymentDto) {
        Payment payment = paymentRepository.save(PaymentDto.mapDtoToEntity(paymentDto));
        return PaymentDto.mapEntityToDto(payment);
    }

    public String pay(String name, String apiKey, String secret, Long walletId, Long userId, Double amount) {
        Site site = SiteDto.mapDtoToEntity(siteService.getSiteByName(name));
        WalletDto wallet = walletService.getWallet(walletId);
        if (apiKey == null || secret == null) {
            throw new RuntimeException("Api Key and Secret shouldn't be null");
        }

        boolean areKeysEqual = Objects.equals(apiKey, site.getApiKey());
        boolean areSecretsEqual = Objects.equals(secret, site.getApiSecret());

        if (!areKeysEqual || !areSecretsEqual) {
            throw new RuntimeException("Request is not valid");
        }

        if (wallet.getBalance() < amount) {
            throw new RuntimeException("insufficient funds");
        }

        wallet.setBalance(wallet.getBalance() - amount);
        walletService.updateWallet(walletId, wallet);
        Wallet siteWallet = site.getWallet();
        siteWallet.setBalance(siteWallet.getBalance() + amount);
        walletService.updateWallet(siteWallet.getId(), WalletDto.mapEntityToDto(siteWallet));
        User user = UserDto.mapDtoToEntity(userService.getUser(userId));
        String key = UUID.randomUUID().toString().replace("-", "");
        Payment payment = Payment.builder()
                .user(user)
                .date(new Date())
                .key(key)
                .amount(amount)
                .build();
        addPayment(PaymentDto.mapEntityToDto(payment));
        return key;
    }

    public boolean validatePayment(String apiKey, String secret, ValidationRequestDto validationRequestDto) {
        if (apiKey == null || secret == null) {
            throw new RuntimeException("Api Key and Secret shouldn't be null");
        }

        String siteName = validationRequestDto.getSiteName();
        SiteDto siteDto = siteService.getSiteByName(siteName);

        boolean areKeysEqual = Objects.equals(apiKey, siteDto.getApiKey());
        boolean areSecretsEqual = Objects.equals(secret, siteDto.getApiSecret());

        if (!areKeysEqual || !areSecretsEqual) {
            throw new RuntimeException("Request is not valid");
        }
        return paymentRepository.existsByKey(validationRequestDto.getKey());
    }

}
