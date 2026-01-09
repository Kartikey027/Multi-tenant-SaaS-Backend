package com.kartikey.saas.policy.entity;

import com.kartikey.saas.common.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "policies")
public class Policy extends BaseEntity {

    @Column(nullable = false)
    private String resource;

    @Column(nullable = false)
    private String action;
}
