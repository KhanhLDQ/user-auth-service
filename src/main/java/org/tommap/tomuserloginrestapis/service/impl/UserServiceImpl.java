package org.tommap.tomuserloginrestapis.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
            throw new RuntimeException(String.format("User already existed - email: %s", userDto.getEmail()));
        }

        userDto.setEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setUserId(userUtils.generateUserId());

        var savedUser = userRepository.save(userMapper.userDtoToUserEntity(userDto));
        return userMapper.userEntityToUserDto(savedUser);
    }

    @Override
    public UserDto getByUsername(String username) {
        var user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException(String.format("User %s not found", username)));

        return userMapper.userEntityToUserDto(user);
    }
}
