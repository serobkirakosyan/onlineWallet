package com.aca.acaonlinewallet.auth;

import com.aca.acaonlinewallet.dto.UserDto;
import com.aca.acaonlinewallet.entity.User;
import com.aca.acaonlinewallet.repository.UserRepository;
import com.aca.acaonlinewallet.util.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public User register(RegisterRequest request) {
        User user = User.builder()
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .birthDate(request.getBirthDate())
                .address(request.getAddress())
                .gender(request.getGender())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserDto.Role.USER.getValue())
                .build();
        return userRepository.save(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        CurrentUser currentUser = CurrentUser.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .walletId(user.getWallet().getId())
                .build();
        String jwtToken = jwtService.generateToken(currentUser.getUsername());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

}