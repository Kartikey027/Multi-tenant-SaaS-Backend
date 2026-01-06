package com.kartikey.saas.auth.controller;

import com.kartikey.saas.auth.controller.dto.LoginRequest;
import com.kartikey.saas.auth.controller.dto.LoginResponse;
import com.kartikey.saas.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@RequestBody @Valid LoginRequest request){
        String token= authService.login(
                request.getEmail(),
                request.getPassword()
        );

        return LoginResponse.builder()
                .accessToken(token)
                .build();
    }
}
