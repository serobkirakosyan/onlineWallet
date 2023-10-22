package com.aca.acaonlinewallet.controller;

import com.aca.acaonlinewallet.auth.CurrentUser;
import com.aca.acaonlinewallet.dto.UserDto;
import com.aca.acaonlinewallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet_v1/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get")
    @PreAuthorize("hasRole('USER')")
    public UserDto getUser(@AuthenticationPrincipal CurrentUser currentUser) {
        return userService.getUser(currentUser.getId());
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('USER')")
    public UserDto updateUser(@AuthenticationPrincipal CurrentUser currentUser,
                              @RequestBody UserDto userDto) {
        return userService.updateUser(currentUser.getId(), userDto);
    }

}