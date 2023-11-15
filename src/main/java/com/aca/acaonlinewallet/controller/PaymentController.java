package com.aca.acaonlinewallet.controller;

import com.aca.acaonlinewallet.auth.CurrentUser;
import com.aca.acaonlinewallet.dto.ValidationRequestDto;
import com.aca.acaonlinewallet.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wallet_v1/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/pay")
    public ResponseEntity<String> pay(@RequestParam String name,
                                      @RequestHeader("ApiKey") String apiKey,
                                      @RequestHeader("ApiSecret") String secret,
                                      @RequestParam Double amount,
                                      @AuthenticationPrincipal CurrentUser currentUser) {
        return ResponseEntity.ok(paymentService.pay(name, apiKey, secret, currentUser.getWalletId(), currentUser.getId(), amount));
    }

    @PostMapping("/validate")
    public boolean validatePayment(@RequestBody ValidationRequestDto validationRequestDto,
                                   @RequestHeader("ApiKey") String apiKey,
                                   @RequestHeader("ApiSecret") String secret) {
        return paymentService.validatePayment(apiKey, secret, validationRequestDto);
    }

}
