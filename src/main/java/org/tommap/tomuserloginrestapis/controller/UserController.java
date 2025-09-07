package org.tommap.tomuserloginrestapis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tommap.tomuserloginrestapis.mapper.UserMapper;
import org.tommap.tomuserloginrestapis.model.request.UserDetailsRequest;
import org.tommap.tomuserloginrestapis.model.response.UserRest;
import org.tommap.tomuserloginrestapis.service.IUserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public String getUserDetails() {
        return "get user details was called";
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequest request) {
        var userDto = userService.createUser(userMapper.requestToUserDto(request));
        return userMapper.userDtoToResponse(userDto);
    }

    @PutMapping
    public String updateUser() {
        return "update user was called";
    }

    @DeleteMapping
    public String deleteUser() {
        return "delete user was called";
    }
}
