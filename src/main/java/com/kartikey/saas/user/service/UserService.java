package com.kartikey.saas.user.service;

import com.kartikey.saas.common.exception.ResourceNotFoundException;
import com.kartikey.saas.tenant.entity.Tenant;
import com.kartikey.saas.tenant.entity.TenantStatus;
import com.kartikey.saas.tenant.repository.TenantRepo;
import com.kartikey.saas.user.entity.User;
import com.kartikey.saas.user.entity.UserStatus;
import com.kartikey.saas.user.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepo userRepo;
    private final TenantRepo tenantRepo;
    private final PasswordEncoder passwordEncoder;

    public User createUser(
            UUID tenantId,
            String email,
            String rawPassword
    ){
        Tenant tenant=tenantRepo
                .findByTenantIdAndStatus(tenantId, TenantStatus.ACTIVE)
                .orElseThrow(()->
                        new ResourceNotFoundException(
                                "Active Tenant Not Found For TenantId: "+ tenantId
                        )
                        );
        if (userRepo.existsByTenant_TenantIdAndEmail(tenantId,email)){
            throw new IllegalArgumentException(
                    "User with Email already exists in this Tenant"
            );
        }

        User user=User.builder()
                .tenant(tenant)
                .email(email)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .build();
        return userRepo.save(user);
    }

    @Transactional(readOnly = true)
    public User getActiveUserByEmail(
            UUID tenantId,
            String email
    ){
        return userRepo
                .findByTenant_TenantIdAndEmailAndStatus(
                        tenantId,
                        email,
                        UserStatus.ACTIVE
                )
                .orElseThrow(()->
                        new ResourceNotFoundException(
                                "Active Users not found for this Email: "+email
                        )
                        );
    }

    public void DisableUser(
            UUID tenantId,
            String email
    ){
        User user=userRepo
                .findByTenant_TenantIdAndEmail(tenantId,email)
                .orElseThrow(()->
                        new ResourceNotFoundException(
                                "User Not Found For The Tenant"
                        )
                        );
        user.setStatus(UserStatus.DISABLED);
    }

    public void enableUser(UUID tenantId, Long userId) {
        User user = userRepo
                .findByTenant_TenantIdAndId(tenantId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found for tenant")
                );

        if (user.getStatus() == UserStatus.ACTIVE) {
            return; // idempotent: already enabled
        }

        user.setStatus(UserStatus.ACTIVE);
    }
}
