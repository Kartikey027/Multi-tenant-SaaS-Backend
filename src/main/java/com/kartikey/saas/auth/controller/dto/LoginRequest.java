package com.kartikey.saas.auth.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class LoginRequest {

    @NotBlank
    private UUID tenantId;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
