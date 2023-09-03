package com.aca.acaonlinewallet.service;

import com.aca.acaonlinewallet.dto.WalletDto;
import com.aca.acaonlinewallet.entity.Wallet;
import com.aca.acaonlinewallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {
    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public WalletDto getWallet(Long id) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new RuntimeException("Wallet by id " + id + " is not found"));
        return WalletDto.mapEntityToDto(wallet);
    }

    @Transactional
    public WalletDto addWallet(WalletDto walletDto) {

        if (walletDto == null) {
            throw new RuntimeException("Wallet can't be null");
        }

        Wallet wallet = WalletDto.mapDtoToEntity(walletDto);

        return WalletDto.mapEntityToDto(walletRepository.save(wallet));
    }

    @Transactional
    public void deleteWallet(Long id) {
        boolean existsById = walletRepository.existsById(id);
        if (!existsById) {
            throw new RuntimeException("Wallet by id" + id + "does not exist");
        }
        walletRepository.deleteById(id);
    }

    @Transactional
    public WalletDto updateWallet(Long id, WalletDto walletDto) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }

        if (walletDto == null) {
            throw new IllegalArgumentException("walletDto cannot be null");
        }

        walletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wallet by id " + id + " does not exist"));

        Wallet updatedWallet = WalletDto.mapDtoToEntity(walletDto);
        updatedWallet.setId(id);
        updatedWallet = walletRepository.save(updatedWallet);
        return WalletDto.mapEntityToDto(updatedWallet);
    }

}

