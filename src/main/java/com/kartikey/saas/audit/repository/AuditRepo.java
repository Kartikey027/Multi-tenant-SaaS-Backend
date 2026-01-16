package com.kartikey.saas.audit.repository;

import com.kartikey.saas.audit.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepo extends JpaRepository<AuditLog, Long> {
}
