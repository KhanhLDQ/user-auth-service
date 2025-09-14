package org.tommap.tomuserloginrestapis.service;

import org.tommap.tomuserloginrestapis.model.dto.UserDto;

public interface IUserService {
    UserDto createUser(UserDto userDto);
    UserDto getByUsername(String username);
    UserDto getByUserId(String userId);
}
