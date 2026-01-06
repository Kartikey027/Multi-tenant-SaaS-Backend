package com.kartikey.saas.common.tenant;

import java.util.UUID;

public final class TenantContext {

    public static final ThreadLocal<UUID> CURRENT_TENANT = new ThreadLocal<>();

    private TenantContext(){}

    public static void setTenantId(UUID tenantId){
        CURRENT_TENANT.set(tenantId);
    }

    public static UUID getTenantId(){
        return CURRENT_TENANT.get();
    }

    public static void clear(){
        CURRENT_TENANT.remove();
    }
}
