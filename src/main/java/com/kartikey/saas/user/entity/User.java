package com.kartikey.saas.user.entity;

import com.kartikey.saas.common.base.BaseEntity;
import com.kartikey.saas.tenant.entity.Tenant;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"tenantId","email"})
        }
)
public class User extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @PrePersist
    private void initDefaults(){
        if(status==null){
            status=UserStatus.ACTIVE;
        }
    }
}
