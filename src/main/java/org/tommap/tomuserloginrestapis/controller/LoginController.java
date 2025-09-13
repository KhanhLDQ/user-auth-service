package org.tommap.tomuserloginrestapis.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tommap.tomuserloginrestapis.model.request.UserLoginRequest;
import org.tommap.tomuserloginrestapis.model.response.ApiResponse;
import org.tommap.tomuserloginrestapis.model.response.UserLoginResponse;
import org.tommap.tomuserloginrestapis.shared.JwtUtils;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
public class LoginController {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<ApiResponse<UserLoginResponse>> login(
            @RequestBody @Valid UserLoginRequest loginRequest
    ) {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            var jwt = jwtUtils.generateJwtToken(authentication);

            return ResponseEntity.status(OK)
                    .body(ApiResponse.ok(
                            "Successful authentication",
                            UserLoginResponse.builder()
                                    .jwt(jwt)
                                    .username(authentication.getName())
                                    .build())
                    );
        } catch (Exception ex) {
            return ResponseEntity.status(UNAUTHORIZED)
                    .body(ApiResponse.error(UNAUTHORIZED.value(), ex.getMessage()));
        }
    }
}
