package com.kartikey.saas.policy.entity;

import com.kartikey.saas.common.base.BaseEntity;
import io.micrometer.core.annotation.Counted;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "policy_rules")
public class PolicyRule extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id",nullable = false)
    private Policy policy;

    @Column(nullable = false)
    private String condition;
}
