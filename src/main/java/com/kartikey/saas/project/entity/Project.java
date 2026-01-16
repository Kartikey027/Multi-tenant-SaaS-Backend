package com.kartikey.saas.project.entity;

import com.kartikey.saas.common.base.BaseEntity;
import com.kartikey.saas.tenant.entity.Tenant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "projects")
public class Project extends BaseEntity {

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id",nullable = false)
    private Tenant tenant;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status= ProjectStatus.ACTIVE;
}
