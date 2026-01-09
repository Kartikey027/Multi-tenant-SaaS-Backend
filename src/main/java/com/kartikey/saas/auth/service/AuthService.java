package com.kartikey.saas.auth.service;

import com.kartikey.saas.common.exception.ResourceNotFoundException;
import com.kartikey.saas.common.tenant.TenantContext;
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
@Transactional(readOnly = true)
public class AuthService {

    private final TenantRepo tenantRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String login(String email,String rawPassword){
        UUID tenantId= TenantContext.getTenantId();
        if (tenantId == null) {
            throw new IllegalStateException("Tenant context is missing");
        }
        tenantRepo.findByTenantIdAndStatus(tenantId, TenantStatus.ACTIVE)
                .orElseThrow(()->
                        new ResourceNotFoundException("Invalid or Inactive Tenant")
                        );
        User user = userRepo
                .findByTenant_TenantIdAndEmailAndStatus(tenantId,email, UserStatus.ACTIVE)
                .orElseThrow(()->
                        new ResourceNotFoundException("Invalid Credentials")
                );
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())){
            throw new ResourceNotFoundException("Invalid Credentials");
        }

        return jwtService.generateToken(user,tenantId);
    }
}
