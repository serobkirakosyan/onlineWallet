package com.aca.acaonlinewallet.service;

import com.aca.acaonlinewallet.auth.CurrentUser;
import com.aca.acaonlinewallet.entity.User;
import com.aca.acaonlinewallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Loading user by username: {}", username);
        User user = userRepository.findByEmail(username).orElseThrow();
        logger.info("User found: {}", user.getEmail());
        return CurrentUser.builder()
                .email(username)
                .password(user.getPassword())
                .build();
    }

}
