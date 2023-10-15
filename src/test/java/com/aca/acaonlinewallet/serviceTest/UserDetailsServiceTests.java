package com.aca.acaonlinewallet.serviceTest;

import com.aca.acaonlinewallet.entity.User;
import com.aca.acaonlinewallet.entity.Wallet;
import com.aca.acaonlinewallet.repository.UserRepository;
import com.aca.acaonlinewallet.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTests {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsernameWhenUsernameIsNotFound() {

        when(userRepository.findByEmail("email")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userDetailsService.loadUserByUsername("email"));
    }

    @Test
    void loadUserByUsernameWhenUsernameIsCorrect() {
        User user = new User();
        user.setId(1L);
        user.setWallet(new Wallet());
        user.setPassword("password");
        when(userRepository.findByEmail("email")).thenReturn(Optional.of(user));
        UserDetails userDetails = userDetailsService.loadUserByUsername("email");
        assertEquals(userDetails.getPassword(), user.getPassword());
    }

}
