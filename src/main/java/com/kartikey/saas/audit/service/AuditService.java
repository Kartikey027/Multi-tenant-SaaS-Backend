package com.kartikey.saas.audit.service;

import com.kartikey.saas.audit.entity.AuditAction;
import com.kartikey.saas.audit.entity.AuditLog;
import com.kartikey.saas.audit.repository.AuditRepo;
import com.kartikey.saas.common.security.SecurityUtils;
import com.kartikey.saas.tenant.entity.Tenant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuditService {
    private final AuditRepo auditRepo;

    public void log(
            Tenant tenant,
            AuditAction action,
            String entityType,
            Long entityId,
            String metadata
    ) {
        AuditLog log = new AuditLog();
        log.setTenant(tenant);
        log.setActorEmail(SecurityUtils.currentUserEmail());
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setMetadata(metadata);

        auditRepo.save(log);
    }
}
