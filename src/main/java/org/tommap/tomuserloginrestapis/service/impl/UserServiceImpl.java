package org.tommap.tomuserloginrestapis.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tommap.tomuserloginrestapis.exception.ResourceAlreadyExistedException;
import org.tommap.tomuserloginrestapis.exception.ResourceNotFoundException;
import org.tommap.tomuserloginrestapis.mapper.UserMapper;
import org.tommap.tomuserloginrestapis.model.dto.AddressDto;
import org.tommap.tomuserloginrestapis.model.dto.UserDto;
import org.tommap.tomuserloginrestapis.model.entity.User;
import org.tommap.tomuserloginrestapis.repository.UserRepository;
import org.tommap.tomuserloginrestapis.service.IUserService;
import org.tommap.tomuserloginrestapis.shared.UserUtils;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

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

    @Override
    public void deleteUser(String userId) {
        var user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User %s not found", userId)));

        userRepository.delete(user);
    }

    @Override
    public Page<UserDto> getUsers(int page, int size, String sortBy, String sortDir) {
        Sort.Direction sortDirection = sortDir.equalsIgnoreCase("desc") ? DESC : ASC;
        Sort sort = Sort.by(sortDirection, sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> users = userRepository.findAll(pageable);

        return users.map(userMapper::userEntityToUserDto); //user.getAddresses() -> lead to N+1 problem -> 1 user query & 1 count user query & N address queries
    }

    @Override
    public List<AddressDto> getUserAddresses(String userId) {
        var user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User %s not found", userId)));

        return user.getAddresses().stream()
                .map(userMapper::addressEntityToAddressDto)
                .toList();
    }
}
