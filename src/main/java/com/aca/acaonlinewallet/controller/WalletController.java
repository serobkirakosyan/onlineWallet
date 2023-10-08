package com.aca.acaonlinewallet.controller;

import com.aca.acaonlinewallet.auth.CurrentUser;
import com.aca.acaonlinewallet.dto.WalletDto;
import com.aca.acaonlinewallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")

public class WalletController {

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/get")
    public WalletDto getWallet(@AuthenticationPrincipal CurrentUser currentUser) {
        return walletService.getWallet(currentUser.getWalletId());
    }

    @PostMapping("/add")
    public WalletDto addWallet(@RequestBody WalletDto walletDto) {
        return walletService.addWallet(walletDto);
    }

    @PutMapping("/update")
    public WalletDto updateWallet(@RequestBody WalletDto walletDto,
                                  @AuthenticationPrincipal CurrentUser currentUser) {
        return walletService.updateWallet(currentUser.getWalletId(), walletDto);
    }

    @PostMapping("/transferToWallet")
    public void transferToWallet(@RequestParam String walletNumber,
                                 @RequestParam Double amount,
                                 @AuthenticationPrincipal CurrentUser currentUser) {
        walletService.moneyTransfer(currentUser.getId(), walletNumber, amount);
    }

    @PostMapping("/transferToDefaultCard")
    public void transferToDefaultCard(@RequestParam Double amount,
                                      @AuthenticationPrincipal CurrentUser currentUser) {
        walletService.transferToDefaultCard(currentUser.getId(), amount);
    }

    @PostMapping("/transferToCard")
    public void transferToCard(@RequestParam Long cardNumber,
                               @RequestParam Double amount,
                               @AuthenticationPrincipal CurrentUser currentUser) {
        walletService.transferToCard(currentUser.getId(), cardNumber, amount);
    }

}