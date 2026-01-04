package com.kartikey.saas.tenant.repository;

import com.kartikey.saas.tenant.entity.Tenant;
import com.kartikey.saas.tenant.entity.TenantStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepo extends JpaRepository<Tenant, Long> {
    Optional<Tenant> findByTenantId(UUID tenantId);

    Optional<Tenant> findByTenantIdAndStatus(UUID tenantId, TenantStatus status);

    boolean existsByName(String name);

    boolean existsByTenantId(UUID tenantId);
}
