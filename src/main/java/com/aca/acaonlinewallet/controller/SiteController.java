package com.aca.acaonlinewallet.controller;

import com.aca.acaonlinewallet.auth.CurrentUser;
import com.aca.acaonlinewallet.dto.SiteDto;
import com.aca.acaonlinewallet.service.SiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wallet_v1/site")
public class SiteController {
    private final SiteService siteService;

    @PostMapping("/register/{walletId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SiteDto> register(@PathVariable Long walletId, @RequestBody SiteDto siteDto) {
        return ResponseEntity.ok(siteService.register(walletId, siteDto));
    }

}
