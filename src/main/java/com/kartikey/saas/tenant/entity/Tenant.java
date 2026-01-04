package com.kartikey.saas.tenant.entity;

import com.kartikey.saas.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tenants")
public class Tenant extends BaseEntity {

    @Column(nullable = false, unique = true, updatable = false)
    private UUID tenantId;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TenantStatus status;

    @PrePersist
    private void initTenantId(){
        if(tenantId == null){
            tenantId=UUID.randomUUID();
        }
        if (status==null){
            status=TenantStatus.ACTIVE;
        }
    }
}
