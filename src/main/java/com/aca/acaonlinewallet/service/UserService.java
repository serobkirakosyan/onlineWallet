package com.aca.acaonlinewallet.service;

import com.aca.acaonlinewallet.dto.UserDto;
import com.aca.acaonlinewallet.entity.User;
import com.aca.acaonlinewallet.exception.UserNotFoundException;
import com.aca.acaonlinewallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scripting.support.StandardScriptEvalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserDto getUser(Long id) {
        logger.info("Getting user by id: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User by id " + id + " is not found"));
        logger.info("User found: {}", user);
        return UserDto.mapEntityToDto(user);
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        logger.info("Updating user with id: {}", id);
        boolean nullArgumentPassed = id == null || userDto == null;

        if (nullArgumentPassed) {
            throw new UserNotFoundException("id and user can't be null");
        }

        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " doesn't exist"));

        User updatedUser = UserDto.mapDtoToEntity(userDto);
        updatedUser.setId(id);
        logger.info("User updated: {}", updatedUser);
        return UserDto.mapEntityToDto(userRepository.save(updatedUser));
    }

}