package com.kartikey.saas.policy.engine;

import org.springframework.stereotype.Component;

@Component
public class PolicyEvaluator {

    public Boolean isAllowed(
            String resource,
            String action,
            AuthorizationContext context
    ){
        if ("USER".equals(resource) && "DISABLE".equals(action)){
            return context.isTenantAdmin();
        }
        return false;
    }
}
