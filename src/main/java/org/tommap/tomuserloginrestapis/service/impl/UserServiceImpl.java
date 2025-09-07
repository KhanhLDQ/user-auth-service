package org.tommap.tomuserloginrestapis.service.impl;

import lombok.RequiredArgsConstructor;
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

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException(String.format("User already existed - email: %s", userDto.getEmail()));
        }

        //hard code first
        userDto.setEncryptedPassword("encrypted_test_pw");
        userDto.setUserId(userUtils.generateUserId());

        var savedUser = userRepository.save(userMapper.userDtoToUserEntity(userDto));
        return userMapper.userEntityToUserDto(savedUser);
    }
}
