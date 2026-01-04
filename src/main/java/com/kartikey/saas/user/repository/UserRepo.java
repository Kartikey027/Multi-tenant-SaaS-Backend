package com.kartikey.saas.user.repository;

import com.kartikey.saas.user.entity.User;
import com.kartikey.saas.user.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User,Long> {

    Optional<User> findByTenant_TenantIdAndEmail(UUID tenantId,String email);

    Optional<User> findByTenant_TenantIdAndId(
            java.util.UUID tenantId,
            Long userId
    );

    boolean existsByTenant_TenantIdAndEmail(
            java.util.UUID tenantId,
            String email
    );

    Optional<User> findByTenant_TenantIdAndEmailAndStatus(
            java.util.UUID tenantId,
            String email,
            UserStatus status
    );
}
