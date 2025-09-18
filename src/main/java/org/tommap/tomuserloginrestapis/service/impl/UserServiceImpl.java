package org.tommap.tomuserloginrestapis.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tommap.tomuserloginrestapis.exception.ResourceAlreadyExistedException;
import org.tommap.tomuserloginrestapis.exception.ResourceNotFoundException;
import org.tommap.tomuserloginrestapis.mapper.UserMapper;
import org.tommap.tomuserloginrestapis.model.dto.UserDto;
import org.tommap.tomuserloginrestapis.repository.UserRepository;
import org.tommap.tomuserloginrestapis.service.IUserService;
import org.tommap.tomuserloginrestapis.shared.UserUtils;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserUtils userUtils;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistedException(String.format("User already existed - email: %s", userDto.getEmail()));
        }

        userDto.setEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setUserId(userUtils.generateUserId());
        var savedUser = userRepository.save(userMapper.userDtoToUserEntity(userDto));

        return userMapper.userEntityToUserDto(savedUser);
    }

    @Override
    public UserDto getByUsername(String username) {
        var user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User %s not found", username)));

        return userMapper.userEntityToUserDto(user);
    }

    @Override
    public UserDto getByUserId(String userId) {
        var user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User %s not found", userId)));

        return userMapper.userEntityToUserDto(user);
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {
        var user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User %s not found", userId)));

        userMapper.updateUserEntityFromUserDto(userDto, user);
        var updatedUser = userRepository.save(user);

        return userMapper.userEntityToUserDto(updatedUser);
    }
}
