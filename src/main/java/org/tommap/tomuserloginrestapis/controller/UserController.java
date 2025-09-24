package org.tommap.tomuserloginrestapis.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tommap.tomuserloginrestapis.mapper.UserMapper;
import org.tommap.tomuserloginrestapis.model.dto.UserDto;
import org.tommap.tomuserloginrestapis.model.request.CreateUserRequest;
import org.tommap.tomuserloginrestapis.model.request.ResendEmailVerificationRequest;
import org.tommap.tomuserloginrestapis.model.request.UpdateUserRequest;
import org.tommap.tomuserloginrestapis.model.response.ApiResponse;
import org.tommap.tomuserloginrestapis.model.response.PageResult;
import org.tommap.tomuserloginrestapis.model.response.UserRest;
import org.tommap.tomuserloginrestapis.service.IUserService;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.tommap.tomuserloginrestapis.model.response.PageResult.PageInfo;

/*
    - each endpoint should represent a specific resource
    - separate into two kinds of endpoints
        + [/users] | [/users/{userId}]
        + [/users/{userId}/addresses] | [/users/{userId}/addresses/{addressId}]
 */
@RestController
@RequestMapping(
    path = "/api/v1/users",
    produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE} //restrict to JSON/XML only - no other formats
)
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<UserRest>>> getUsers(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "2") int size,
        @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
        @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir
    ) {
        Page<UserDto> users = userService.getUsers(page, size, sortBy, sortDir);
        Page<UserRest> userRests = users.map(userMapper::userDtoToResponse);

        var response = PageResult.<UserRest>builder()
                .content(userRests.getContent())
                .pageInfo(PageInfo.builder()
                        .currentPage(userRests.getNumber())
                        .numOfElements(userRests.getNumberOfElements())
                        .pageSize(userRests.getSize())
                        .totalPages(userRests.getTotalPages())
                        .totalElements(userRests.getTotalElements())
                        .hasPrevious(userRests.hasPrevious())
                        .hasNext(userRests.hasNext())
                        .build()
                )
                .build();

        return ResponseEntity.ok(ApiResponse.ok("Get users successfully", response));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserRest>> getUserDetails(@PathVariable(name = "userId") String userId) {
        var userDto = userService.getByUserId(userId);
        var userRest = userMapper.userDtoToResponse(userDto);

        return ResponseEntity.ok(
                ApiResponse.ok("Get user details successfully", userRest)
        );
    }

    @GetMapping("/{userId}/addresses")
    public ResponseEntity<ApiResponse<List<UserRest.AddressRest>>> getUserAddresses(
        @PathVariable("userId") String userId
    ) {
        var addressDtos = userService.getUserAddresses(userId);
        var addressRests = addressDtos.stream()
                .map(userMapper::addressDtoToResponse)
                .toList();

        return ResponseEntity.ok(
                ApiResponse.ok("Get user addresses successfully", addressRests)
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

        return ResponseEntity.ok(
                ApiResponse.ok("User updated successfully", userRest)
        );
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
        @PathVariable("userId") String userId
    ) {
        userService.deleteUser(userId);

        return ResponseEntity.ok(
                ApiResponse.ok("User deleted successfully", null)
        );
    }

    @GetMapping("/email-verification")
    public ResponseEntity<ApiResponse<Void>> verifyEmailToken(
        @RequestParam("token") String token
    ) {
        var isEmailVerified = userService.verifyEmailToken(token);

        if (isEmailVerified) {
            return ResponseEntity.ok(ApiResponse.ok("Email verified successfully", null));
        }

        return ResponseEntity.badRequest()
                .body(ApiResponse.error(BAD_REQUEST.value(), "Invalid or expired verification token"));
    }

    @PostMapping("/resend-email-verification")
    public ResponseEntity<ApiResponse<Void>> resendEmailVerification(
        @RequestBody @Valid ResendEmailVerificationRequest request
    ) {
        userService.resendEmailVerification(request.getEmail());

        return ResponseEntity.ok(
                ApiResponse.ok("Resend email verification successfully", null)
        );
    }
}
