package com.kartikey.saas.project.repository;

import com.kartikey.saas.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepo extends JpaRepository<Project, Long> {
    List<Project> findByTenant_TenantId(UUID tenantId);

    Optional<Project> findByTenant_TenantIdAndId(UUID tenantId, Long projectId);
}
