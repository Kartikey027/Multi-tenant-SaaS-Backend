package com.kartikey.saas.tenant.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTenantRequest {

    @NotBlank
    private String name;
}
