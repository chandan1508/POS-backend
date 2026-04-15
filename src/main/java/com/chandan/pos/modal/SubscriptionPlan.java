package com.chandan.pos.modal;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.chandan.pos.domain.BillingCycle;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "subscription_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillingCycle billingCycle;

    private Integer maxBranches;
    private Integer maxUsers;
    private Integer maxProducts;

    @Column(nullable = false)
    private Boolean enableAdvancedReports = false;

    @Column(nullable = false)
    private Boolean enableInventory = false;

    @Column(nullable = false)
    private Boolean enableIntegrations = false;

    @Column(nullable = false)
    private Boolean enableEcommerce = false;

    @Column(nullable = false)
    private Boolean enableInvoiceBranding = false;

    @Column(nullable = false)
    private Boolean prioritySupport = false;

    @Column(nullable = false)
    private Boolean enableMultiLocation = false;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "subscription_plan_extra_features",
            joinColumns = @JoinColumn(name = "plan_id"))
    @Column(name = "feature")
    private List<String> extraFeatures;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
