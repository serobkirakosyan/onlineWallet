package com.aca.acaonlinewallet.controller;

import com.aca.acaonlinewallet.dto.WalletDto;
import com.aca.acaonlinewallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")

public class WalletController {

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/get/{id}")
    public WalletDto getWallet(@PathVariable Long id) {
        return walletService.getWallet(id);
    }

    @PostMapping("/add")
    public WalletDto addWallet(@RequestBody WalletDto walletDto) {
        return walletService.addWallet(walletDto);
    }

    @PutMapping("update/{id}")
    public WalletDto updateWallet(@PathVariable Long id, @RequestBody WalletDto walletDto) {
        return walletService.updateWallet(id, walletDto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteWallet(@PathVariable Long id) {
        walletService.deleteWallet(id);
    }

}
