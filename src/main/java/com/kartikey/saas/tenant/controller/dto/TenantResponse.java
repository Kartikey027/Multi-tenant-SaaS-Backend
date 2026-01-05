package com.kartikey.saas.tenant.controller.dto;

import com.kartikey.saas.tenant.entity.TenantStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class TenantResponse {
    private UUID tenantId;
    private String name;
    private TenantStatus status;
}
