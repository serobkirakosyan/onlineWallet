package com.aca.acaonlinewallet.service;

import com.aca.acaonlinewallet.dto.UserDto;
import com.aca.acaonlinewallet.entity.User;
import com.aca.acaonlinewallet.exception.UserNotFoundException;
import com.aca.acaonlinewallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scripting.support.StandardScriptEvalException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User by id " + id + " is not found"));
        return UserDto.mapEntityToDto(user);
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        boolean nullArgumentPassed = id == null || userDto == null;

        if (nullArgumentPassed) {
            throw new UserNotFoundException("id and user can't be null");
        }

        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " doesn't exist"));

        User updatedUser = UserDto.mapDtoToEntity(userDto);
        updatedUser.setId(id);
        return UserDto.mapEntityToDto(userRepository.save(updatedUser));
    }

}