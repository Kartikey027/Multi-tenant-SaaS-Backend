package com.kartikey.saas.user.policy;

import com.kartikey.saas.common.tenant.TenantContext;
import com.kartikey.saas.user.entity.User;
import com.kartikey.saas.user.entity.UserStatus;
import com.kartikey.saas.user.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserPolicy {

    private final UserRepo userRepo;

    public boolean canPerform(
            UserAction action,
            UserActionContext ctx
    ){
        if (!isTenantAdmin(ctx.getCurrentUser())){
            return false;
        }

        if (ctx.getCurrentUser().getId() == (ctx.getTargetUser().getId())){
            return false;
        }

        return switch (action){
            case DISABLE -> ctx.getTargetUser().getStatus() != UserStatus.DISABLED;
            case ENABLE -> ctx.getTargetUser().getStatus() != UserStatus.ACTIVE;
        };
    }

    private boolean isTenantAdmin(User currentUser) {

        UUID tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            return false;
        }

        return userRepo
                .findFirstByTenant_TenantIdOrderByCreatedAtAsc(tenantId)
                .map(admin -> admin.getId()== currentUser.getId())
                .orElse(false);
    }
}
