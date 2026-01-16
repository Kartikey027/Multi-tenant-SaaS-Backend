package com.kartikey.saas.user.service;

import com.kartikey.saas.audit.entity.AuditAction;
import com.kartikey.saas.audit.service.AuditService;
import com.kartikey.saas.common.exception.ForbiddenOperationException;
import com.kartikey.saas.common.exception.ResourceNotFoundException;
import com.kartikey.saas.common.exception.UnauthorizedException;
import com.kartikey.saas.common.tenant.TenantContext;
import com.kartikey.saas.policy.engine.AuthorizationContext;
import com.kartikey.saas.policy.engine.PolicyEvaluator;
import com.kartikey.saas.tenant.entity.Tenant;
import com.kartikey.saas.tenant.entity.TenantStatus;
import com.kartikey.saas.tenant.repository.TenantRepo;
import com.kartikey.saas.user.entity.User;
import com.kartikey.saas.user.entity.UserStatus;
import com.kartikey.saas.user.policy.UserAction;
import com.kartikey.saas.user.policy.UserActionContext;
import com.kartikey.saas.user.policy.UserPolicy;
import com.kartikey.saas.user.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserPolicy userPolicy;
    private final AuditService auditService;

    public User createUser(
            String email,
            String rawPassword
    ){
        UUID tenantId= TenantContext.getTenantId();
        if (tenantId == null) {
            throw new ForbiddenOperationException("Tenant context is missing");
        }
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
        if (tenantId == null) {
            throw new ForbiddenOperationException("Tenant context is missing");
        }
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

    @Transactional
    public void disableUser(
            Long userId
    ){
        UserActionContext ctx= resolveUserActionContext(userId);

        if (!userPolicy.canPerform(UserAction.DISABLE,ctx)){
            throw new ForbiddenOperationException("Not Allowed to disable user");
        }

        ctx.getTargetUser().setStatus(UserStatus.DISABLED);


        auditService.log(
                ctx.getTargetUser().getTenant(),
                AuditAction.DISABLE,
                "USER",
                ctx.getTargetUser().getId(),
                "User disabled"
        );
    }

    @Transactional
    public void enableUser(Long userId) {
        UserActionContext ctx = resolveUserActionContext(userId);

        if (!userPolicy.canPerform(UserAction.ENABLE, ctx)) {
            throw new ForbiddenOperationException("Not allowed to enable user");
        }

        ctx.getTargetUser().setStatus(UserStatus.ACTIVE);


        auditService.log(
                ctx.getTargetUser().getTenant(),
                AuditAction.ENABLE,
                "USER",
                ctx.getTargetUser().getId(),
                "User enabled"
        );
    }

   private UserActionContext resolveUserActionContext(Long targetUserId){
        UUID tenantId=TenantContext.getTenantId();
        if (tenantId==null){
            throw new ForbiddenOperationException("Tenant Context Missing");
        }

        String currentUserEmail=SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User currentUser=userRepo
                .findByTenant_TenantIdAndEmail(tenantId,currentUserEmail)
                .orElseThrow(()->
                        new ForbiddenOperationException(
                                "Authenticated User not Found"
                        )
                );

        User targetUser=userRepo
                .findByTenant_TenantIdAndId(tenantId,targetUserId)
                .orElseThrow(()->
                        new ResourceNotFoundException(
                                "User Not Found in Tenant"
                        )
                        );
        return new UserActionContext(currentUser,targetUser);
   }
}
