package com.aca.acaonlinewallet.controller;

import com.aca.acaonlinewallet.auth.CurrentUser;
import com.aca.acaonlinewallet.dto.UtilityPaymentDto;
import com.aca.acaonlinewallet.service.UtilityPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallet_v1/utility")
@RequiredArgsConstructor
public class UtilityPaymentController {
    private final UtilityPaymentService utilityPaymentService;

    @GetMapping("/getUserUtilities")
    public ResponseEntity<List<UtilityPaymentDto>> getUserUtilities(@AuthenticationPrincipal CurrentUser currentUser) {
        return ResponseEntity.ok(utilityPaymentService.getUserUtilities(currentUser.getId()));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<UtilityPaymentDto> getUtilityPayment(@PathVariable Long id) {
        return ResponseEntity.ok(utilityPaymentService.getUtilityPayment(id));
    }

    @PostMapping("/add")
    public ResponseEntity<UtilityPaymentDto> addUtilityPayment(@RequestBody UtilityPaymentDto utilityPaymentDto,
                                                               @AuthenticationPrincipal CurrentUser currentUser) {
        return ResponseEntity.ok(utilityPaymentService.addUtilityPayment(utilityPaymentDto, currentUser.getId()));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UtilityPaymentDto> updateUtilityPayment(@PathVariable Long id,
                                                                  @RequestBody UtilityPaymentDto utilityPaymentDto) {
        return ResponseEntity.ok(utilityPaymentService.updateUtilityPayment(id, utilityPaymentDto));
    }

    @DeleteMapping("/delete/{id}")
    public void removeUtilityPayment(@PathVariable Long id) {
        utilityPaymentService.removeUtility(id);
    }

    @PutMapping("/pay")
    public ResponseEntity<UtilityPaymentDto> payUtility(@RequestParam String type,
                                                        @RequestParam Double amount,
                                                        @AuthenticationPrincipal CurrentUser currentUser) {
        return ResponseEntity.ok(utilityPaymentService.payUtility(type, amount, currentUser.getId(), currentUser.getWalletId()));
    }

}