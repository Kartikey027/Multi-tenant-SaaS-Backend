package com.kartikey.saas.user.service;

import com.kartikey.saas.common.exception.ResourceNotFoundException;
import com.kartikey.saas.common.tenant.TenantContext;
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
            String email,
            String rawPassword
    ){
        UUID tenantId= TenantContext.getTenantId();
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
            String email
    ){
        UUID tenantId= TenantContext.getTenantId();
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

    public void disableUser(
            Long userId
    ){
        UUID tenantId= TenantContext.getTenantId();
        User user=userRepo
                .findByTenant_TenantIdAndId(tenantId,userId)
                .orElseThrow(()->
                        new ResourceNotFoundException(
                                "User Not Found For The Tenant"
                        )
                        );
        user.setStatus(UserStatus.DISABLED);
    }

    public void enableUser(Long userId) {
        UUID tenantId= TenantContext.getTenantId();
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
