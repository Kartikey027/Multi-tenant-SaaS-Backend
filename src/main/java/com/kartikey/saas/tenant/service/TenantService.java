package com.kartikey.saas.tenant.service;


import com.kartikey.saas.common.exception.ResourceNotFoundException;
import com.kartikey.saas.tenant.entity.Tenant;
import com.kartikey.saas.tenant.entity.TenantStatus;
import com.kartikey.saas.tenant.repository.TenantRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepo tenantRepo;

    public Tenant createTenant(String name){
        if(tenantRepo.existsByName(name)){
            throw new IllegalArgumentException("Tenant With Name Already Exists");
        }
        Tenant tenant=Tenant.builder()
                .name(name)
                .status(TenantStatus.ACTIVE)
                .build();
        return tenantRepo.save(tenant);
    }

    @Transactional(readOnly = true)
    public Tenant getActiveTenantByTenantId(UUID tenantId){
        return tenantRepo
                .findByTenantIdAndStatus(tenantId,TenantStatus.ACTIVE)
                .orElseThrow(()->
                        new ResourceNotFoundException(
                                "Active Tenant Not Found for TenantId:"+ tenantId
                        )
                );
    }

    public void suspendTenant(UUID tenantId){
        Tenant tenant=tenantRepo
                .findByTenantId(tenantId)
                .orElseThrow(()->
                        new ResourceNotFoundException(
                                "Tenant Not Found for TenantId"+ tenantId
                        )
                        );
        tenant.setStatus(TenantStatus.SUSPENDED);
    }

    public void activateTenant(UUID tenantId) {
        Tenant tenant = tenantRepo
                .findByTenantId(tenantId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Tenant not found for tenantId: " + tenantId
                        )
                );

        if (tenant.getStatus() == TenantStatus.DELETED) {
            throw new IllegalStateException("Deleted tenant cannot be reactivated");
        }

        tenant.setStatus(TenantStatus.ACTIVE);
    }
}
