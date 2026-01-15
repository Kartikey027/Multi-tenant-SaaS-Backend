package com.kartikey.saas.user.policy;

import com.kartikey.saas.common.tenant.TenantContext;
import com.kartikey.saas.user.entity.User;
import com.kartikey.saas.user.entity.UserStatus;
import com.kartikey.saas.user.repository.UserRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UserPolicyTest {
    private UserRepo userRepo;
    private UserPolicy userPolicy;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        userRepo = Mockito.mock(UserRepo.class);
        userPolicy = new UserPolicy(userRepo);

        tenantId = UUID.randomUUID();
        TenantContext.setTenantId(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    private User user(Long id, String email, UserStatus status) {
        User u = new User();
        u.setId(id);
        u.setEmail(email);
        u.setStatus(status);
        return u;
    }

    @Test
    void admin_can_disable_other_user() {

        User admin = user(1L, "admin@test.com", UserStatus.ACTIVE);
        User target = user(2L, "user@test.com", UserStatus.ACTIVE);

        Mockito.when(
                userRepo.findFirstByTenant_TenantIdOrderByCreatedAtAsc(tenantId)
        ).thenReturn(Optional.of(admin));

        UserActionContext ctx = new UserActionContext(admin, target);

        boolean allowed =
                userPolicy.canPerform(UserAction.DISABLE, ctx);

        assertThat(allowed).isTrue();
    }

    @Test
    void admin_cannot_disable_self() {

        User admin = user(1L, "admin@test.com", UserStatus.ACTIVE);

        Mockito.when(
                userRepo.findFirstByTenant_TenantIdOrderByCreatedAtAsc(tenantId)
        ).thenReturn(Optional.of(admin));

        UserActionContext ctx = new UserActionContext(admin, admin);

        boolean allowed =
                userPolicy.canPerform(UserAction.DISABLE, ctx);

        assertThat(allowed).isFalse();
    }

    @Test
    void non_admin_cannot_disable_user() {

        User admin = user(1L, "admin@test.com", UserStatus.ACTIVE);
        User nonAdmin = user(2L, "user@test.com", UserStatus.ACTIVE);
        User target = user(3L, "other@test.com", UserStatus.ACTIVE);

        Mockito.when(
                userRepo.findFirstByTenant_TenantIdOrderByCreatedAtAsc(tenantId)
        ).thenReturn(Optional.of(admin));

        UserActionContext ctx =
                new UserActionContext(nonAdmin, target);

        boolean allowed =
                userPolicy.canPerform(UserAction.DISABLE, ctx);

        assertThat(allowed).isFalse();
    }



}
