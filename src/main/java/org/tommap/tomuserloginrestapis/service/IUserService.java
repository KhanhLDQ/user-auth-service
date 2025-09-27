package org.tommap.tomuserloginrestapis.service;

import org.springframework.data.domain.Page;
import org.tommap.tomuserloginrestapis.model.dto.AddressDto;
import org.tommap.tomuserloginrestapis.model.dto.UserDto;

import java.util.List;

public interface IUserService {
    UserDto createUser(UserDto userDto);
    UserDto getByUsername(String username);
    UserDto getByUserId(String userId);
    UserDto updateUser(String userId, UserDto userDto);
    void deleteUser(String userId);
    Page<UserDto> getUsers(int page, int size, String sortBy, String sortDir);
    List<AddressDto> getUserAddresses(String userId);
    boolean verifyEmailToken(String token);
    void resendEmailVerification(String email);
    void resetPasswordRequest(String email);
    void resetPassword(String emailToken, String newPassword);
}
