package com.aca.acaonlinewallet.serviceTest;

import com.aca.acaonlinewallet.dto.UserDto;
import com.aca.acaonlinewallet.dto.WalletDto;
import com.aca.acaonlinewallet.entity.User;
import com.aca.acaonlinewallet.entity.Wallet;
import com.aca.acaonlinewallet.repository.UserRepository;
import com.aca.acaonlinewallet.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void getUserWhenIdIsNotFound() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.getUser(id));
    }

    @Test
    void getUserWhenIdIsCorrect() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setWallet(new Wallet());

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        UserDto result = userService.getUser(id);
        assertEquals(result.getId(), user.getId());
    }

    @Test
    void updateUserWhenIdIsIncorrect() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.updateUser(1L, new UserDto()));
    }

    @Test
    void updateUserWhenIdAndDtoIsCorrect() {

        User user = new User();
        user.setId(1L);
        user.setWallet(new Wallet());

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setWalletDto(new WalletDto());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserDto result = userService.updateUser(1L, userDto);
        assertEquals(result.getId(), user.getId());

    }

}
