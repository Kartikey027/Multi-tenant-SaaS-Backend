package com.kartikey.saas.tenant.controller;

import com.kartikey.saas.tenant.controller.dto.CreateTenantRequest;
import com.kartikey.saas.tenant.controller.dto.TenantResponse;
import com.kartikey.saas.tenant.entity.Tenant;
import com.kartikey.saas.tenant.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("internal/admin/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TenantResponse createTenant(
            @RequestBody CreateTenantRequest request
            ){
        Tenant tenant=tenantService.createTenant(request.getName());

        return TenantResponse.builder()
                .tenantId(tenant.getTenantId())
                .name(tenant.getName())
                .status(tenant.getStatus())
                .build();
    }

    @PostMapping("/{tenantId}/suspend")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void suspendTenant(
            @PathVariable UUID tenantId
            ){
        tenantService.suspendTenant(tenantId);
    }

    @PostMapping("/{tenantId}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activateTenant(
            @PathVariable UUID tenantId
    ){
        tenantService.activateTenant(tenantId);
    }

    @GetMapping("/{tenantId}")
    public Tenant getTenant(
            @PathVariable UUID tenantId
    ){
        return tenantService.getTenantByTenantId(tenantId);
    }
}
