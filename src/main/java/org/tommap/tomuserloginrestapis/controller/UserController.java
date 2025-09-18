package org.tommap.tomuserloginrestapis.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tommap.tomuserloginrestapis.mapper.UserMapper;
import org.tommap.tomuserloginrestapis.model.request.CreateUserRequest;
import org.tommap.tomuserloginrestapis.model.request.UpdateUserRequest;
import org.tommap.tomuserloginrestapis.model.response.ApiResponse;
import org.tommap.tomuserloginrestapis.model.response.UserRest;
import org.tommap.tomuserloginrestapis.service.IUserService;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(
    path = "/api/v1/users",
    produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE} //restrict to JSON/XML only - no other formats
)
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserRest>> getUserDetails(@PathVariable(name = "userId") String userId) {
        var userDto = userService.getByUserId(userId);
        var userRest = userMapper.userDtoToResponse(userDto);

        return ResponseEntity.ok(
                ApiResponse.ok("Get user details successfully", userRest)
        );
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<ApiResponse<UserRest>> createUser(@RequestBody @Valid CreateUserRequest request) {
        var userDto = userService.createUser(userMapper.createUserRequestToUserDto(request));
        var userRest = userMapper.userDtoToResponse(userDto);

        return ResponseEntity.status(CREATED)
                .body(
                    ApiResponse.created("User created successfully", userRest)
                );
    }

    @PutMapping(
        path = "/{userId}",
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<ApiResponse<UserRest>> updateUser(
        @PathVariable("userId") String userId,
        @RequestBody @Valid UpdateUserRequest request
    ) {
        var userDto = userService.updateUser(userId, userMapper.updateUserRequestToUserDto(request));
        var userRest = userMapper.userDtoToResponse(userDto);

        return ResponseEntity.ok(ApiResponse.ok("User updated successfully", userRest));
    }

    @DeleteMapping
    public String deleteUser() {
        return "delete user was called";
    }
}
