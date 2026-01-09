package com.kartikey.saas.policy.engine;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthorizationContext {

    private Long uesrId;
    private boolean tenantAdmin;
    private boolean systemAdmin;
}
