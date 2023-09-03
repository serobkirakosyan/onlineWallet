package com.aca.acaonlinewallet.service;

import com.aca.acaonlinewallet.dto.UserDto;
import com.aca.acaonlinewallet.entity.User;
import com.aca.acaonlinewallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User by id " + id + " is not found"));
        return UserDto.mapEntityToDto(user);
    }

    @Transactional
    public UserDto addUser(UserDto userDto) {

        if (userDto == null) {
            throw new RuntimeException("User can't be null");
        }

        User user = UserDto.mapDtoToEntity(userDto);
        return UserDto.mapEntityToDto(userRepository.save(user));
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        boolean nullArgumentPassed = id == null || userDto == null;

        if (nullArgumentPassed) {
            throw new RuntimeException("id and user can't be null");
        }

        userRepository.findById(id).orElseThrow(() -> new RuntimeException("User with id " + id + " doesn't exist"));

        User updatedUser = UserDto.mapDtoToEntity(userDto);
        updatedUser.setId(id);
        return UserDto.mapEntityToDto(userRepository.save(updatedUser));
    }

    @Transactional
    public void deleteUserById(Long id) {
        boolean existsById = userRepository.existsById(id);

        if (!existsById) {
            throw new RuntimeException("User with id " + id + " doesn't exist");
        }

        userRepository.deleteById(id);
    }

}